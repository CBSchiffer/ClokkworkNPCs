package com.clokkwork.clokkworknpc.data.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record NpcDefinition(
		ResourceLocation id,
		String displayName,
		NpcModelDefinition model,
		ResourceLocation dialogue,
		List<ResourceLocation> factions,
		List<String> tags
) {

	public static final Codec<NpcDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(NpcDefinition::id),
			Codec.STRING.fieldOf("display_name").forGetter(NpcDefinition::displayName),
			NpcModelDefinition.CODEC.fieldOf("model").forGetter(NpcDefinition::model),
			ResourceLocation.CODEC.fieldOf("dialogue").forGetter(NpcDefinition::dialogue),
			ResourceLocation.CODEC.listOf().fieldOf("factions").forGetter(NpcDefinition::factions),
			Codec.STRING.listOf().fieldOf("tags").forGetter(NpcDefinition::tags)
	).apply(instance, NpcDefinition::new));
}
