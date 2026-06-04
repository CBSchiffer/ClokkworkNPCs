package com.clokkwork.clokkworknpc.data.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record NpcModelDefinition(ResourceLocation type, List<ResourceLocation> skins) {

	public static final Codec<NpcModelDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("type").forGetter(NpcModelDefinition::type),
			ResourceLocation.CODEC.listOf().fieldOf("skins").forGetter(NpcModelDefinition::skins)
	).apply(instance, NpcModelDefinition::new));
}
