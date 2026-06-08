package com.clokkwork.clokkworknpc.entity;

import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinitionDefaults;
import com.clokkwork.clokkworknpc.faction.FactionMembership;
import com.clokkwork.clokkworknpc.npc.INpcHost;
import com.clokkwork.clokkworknpc.npc.NpcAttributeApplicator;
import com.clokkwork.clokkworknpc.npc.NpcDialogueService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Framework-owned generic humanoid NPC. First concrete {@link INpcHost} implementation.
 * Runtime state on this entity is independent of its spawn definition; the definition only supplies initial values.
 */
public class GenericHumanoidNpcEntity extends PathfinderMob implements INpcHost {

	private static final String DEFINITION_ID_TAG = "NpcDefinitionId";
	private static final String SKIN_TAG = "NpcSkin";
	private static final String FACTIONS_TAG = "NpcFactions";
	private static final String TAGS_TAG = "NpcTags";

	private static final EntityDataAccessor<String> DATA_SKIN = SynchedEntityData.defineId(
			GenericHumanoidNpcEntity.class,
			EntityDataSerializers.STRING
	);

	private ResourceLocation npcDefinitionId;
	private FactionMembership factionMembership = new FactionMembership();
	private List<String> tags = List.of();

	public GenericHumanoidNpcEntity(EntityType<? extends GenericHumanoidNpcEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_SKIN, NpcDefinitionDefaults.DEFAULT_STEVE_SKIN.toString());
	}

	@Override
	public Optional<ResourceLocation> getNpcDefinitionId() {
		return Optional.ofNullable(npcDefinitionId);
	}

	@Override
	public void setNpcDefinitionId(ResourceLocation id) {
		this.npcDefinitionId = id;
	}

	@Override
	public FactionMembership getFactionMembership() {
		return factionMembership;
	}

	@Override
	public List<String> getNpcTags() {
		return tags;
	}

	@Override
	public ResourceLocation getSkinTexture() {
		return ResourceLocation.parse(this.entityData.get(DATA_SKIN));
	}

	/**
	 * Applies spawn-template values from a definition to this entity instance.
	 */
	@Override
	public void applyDefinition(NpcDefinition definition) {
		setNpcDefinitionId(definition.id());
		setCustomName(Component.literal(definition.displayName()));
		setCustomNameVisible(true);
		this.factionMembership = FactionMembership.fromInitial(definition.initialFactions());
		this.tags = List.copyOf(definition.tags());
		setSkinTexture(definition.model().primarySkin());
		setNoAi(definition.noAi());
		NpcAttributeApplicator.apply(this, definition.id(), definition.spawnAttributes());
	}

	@Override
	public GenericHumanoidNpcEntity asLivingEntity() {
		return this;
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (this.level().isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		return NpcDialogueService.tryStartDialogue(player, this);
	}

	private void setSkinTexture(ResourceLocation skin) {
		this.entityData.set(DATA_SKIN, skin.toString());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains(DEFINITION_ID_TAG)) {
			this.npcDefinitionId = ResourceLocation.parse(tag.getString(DEFINITION_ID_TAG));
		} else {
			this.npcDefinitionId = null;
		}

		if (tag.contains(SKIN_TAG)) {
			setSkinTexture(ResourceLocation.parse(tag.getString(SKIN_TAG)));
		}

		this.factionMembership = new FactionMembership();
		if (tag.contains(FACTIONS_TAG, Tag.TAG_LIST)) {
			ListTag factionsTag = tag.getList(FACTIONS_TAG, Tag.TAG_STRING);
			for (Tag factionTag : factionsTag) {
				this.factionMembership.add(ResourceLocation.parse(factionTag.getAsString()));
			}
		}

		if (tag.contains(TAGS_TAG, Tag.TAG_LIST)) {
			ListTag tagsTag = tag.getList(TAGS_TAG, Tag.TAG_STRING);
			List<String> loadedTags = new ArrayList<>(tagsTag.size());
			for (Tag tagEntry : tagsTag) {
				loadedTags.add(tagEntry.getAsString());
			}
			this.tags = List.copyOf(loadedTags);
		} else {
			this.tags = List.of();
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.npcDefinitionId != null) {
			tag.putString(DEFINITION_ID_TAG, this.npcDefinitionId.toString());
		}
		tag.putString(SKIN_TAG, getSkinTexture().toString());

		ListTag factionsTag = new ListTag();
		for (ResourceLocation factionId : this.factionMembership.asSet()) {
			factionsTag.add(StringTag.valueOf(factionId.toString()));
		}
		tag.put(FACTIONS_TAG, factionsTag);

		ListTag tagsTag = new ListTag();
		for (String npcTag : this.tags) {
			tagsTag.add(StringTag.valueOf(npcTag));
		}
		tag.put(TAGS_TAG, tagsTag);
	}
}
