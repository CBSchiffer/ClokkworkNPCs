package com.clokkwork.clokkworknpc.data.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/** Faction metadata loaded from data files and/or created during gameplay. */
public record FactionDefinition(
		ResourceLocation id,
		String title,
		Optional<String> description
) {

	public static final Codec<FactionDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(FactionDefinition::id),
			Codec.STRING.fieldOf("title").forGetter(FactionDefinition::title),
			Codec.STRING.optionalFieldOf("description").forGetter(FactionDefinition::description)
	).apply(instance, FactionDefinition::new));
}
