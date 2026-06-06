package com.clokkwork.clokkworknpc.data.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record NpcModelDefinition(ResourceLocation type, List<ResourceLocation> skins) {

	public static final Codec<NpcModelDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.optionalFieldOf("type", NpcDefinitionDefaults.HUMANOID_MODEL_TYPE)
					.forGetter(NpcModelDefinition::type),
			ResourceLocation.CODEC.listOf().optionalFieldOf("skins", List.of()).forGetter(NpcModelDefinition::skins)
	).apply(instance, NpcModelDefinition::new));

	public static NpcModelDefinition defaultHumanoid() {
		return new NpcModelDefinition(
				NpcDefinitionDefaults.HUMANOID_MODEL_TYPE,
				List.of(NpcDefinitionDefaults.DEFAULT_STEVE_SKIN)
		);
	}

	public ResourceLocation primarySkin() {
		return skins.isEmpty() ? NpcDefinitionDefaults.DEFAULT_STEVE_SKIN : skins.getFirst();
	}
}
