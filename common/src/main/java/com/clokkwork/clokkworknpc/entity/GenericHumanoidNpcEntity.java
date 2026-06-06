package com.clokkwork.clokkworknpc.entity;

import com.clokkwork.clokkworknpc.npc.INpcHost;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Framework-owned generic humanoid NPC. First concrete {@link INpcHost} implementation.
 */
public class GenericHumanoidNpcEntity extends PathfinderMob implements INpcHost {

	private static final String DEFINITION_ID_TAG = "NpcDefinitionId";

	private ResourceLocation npcDefinitionId;

	public GenericHumanoidNpcEntity(EntityType<? extends GenericHumanoidNpcEntity> type, Level level) {
		super(type, level);
		this.setNoAi(true);
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
	public GenericHumanoidNpcEntity asLivingEntity() {
		return this;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains(DEFINITION_ID_TAG)) {
			this.npcDefinitionId = ResourceLocation.parse(tag.getString(DEFINITION_ID_TAG));
		} else {
			this.npcDefinitionId = null;
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.npcDefinitionId != null) {
			tag.putString(DEFINITION_ID_TAG, this.npcDefinitionId.toString());
		}
	}
}
