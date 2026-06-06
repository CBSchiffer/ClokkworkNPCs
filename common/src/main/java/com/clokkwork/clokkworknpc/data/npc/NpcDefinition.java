package com.clokkwork.clokkworknpc.data.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Data-driven NPC template. The {@code factions} JSON field is {@link #initialFactions()} only:
 * starting membership when an NPC is first spawned from this definition, not live membership.
 *
 * @see com.clokkwork.clokkworknpc.faction.FactionMembership
 */
public record NpcDefinition(
		ResourceLocation id,
		String displayName,
		NpcModelDefinition model,
		ResourceLocation dialogue,
		List<ResourceLocation> initialFactions,
		List<String> tags
) {

	public static final Codec<NpcDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(NpcDefinition::id),
			Codec.STRING.fieldOf("display_name").forGetter(NpcDefinition::displayName),
			NpcModelDefinition.CODEC.fieldOf("model").forGetter(NpcDefinition::model),
			ResourceLocation.CODEC.fieldOf("dialogue").forGetter(NpcDefinition::dialogue),
			ResourceLocation.CODEC.listOf().fieldOf("factions").forGetter(NpcDefinition::initialFactions),
			Codec.STRING.listOf().fieldOf("tags").forGetter(NpcDefinition::tags)
	).apply(instance, NpcDefinition::new));
}
