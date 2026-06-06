package com.clokkwork.clokkworknpc.data.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

/**
 * Resolved spawn template for a type of NPC. A definition describes how to create NPC instances; it is not the
 * persistent state of any single entity. The same definition may spawn many runtime NPCs.
 * <p>
 * JSON fields such as {@code factions} and {@code tags} are initial spawn conditions only. Live faction membership,
 * dialogue progress, and other runtime state belong on the spawned entity.
 *
 * @see com.clokkwork.clokkworknpc.faction.FactionMembership
 */
public record NpcDefinition(
		ResourceLocation id,
		String displayName,
		NpcModelDefinition model,
		Optional<ResourceLocation> dialogue,
		List<ResourceLocation> initialFactions,
		List<String> tags,
		boolean noAi,
		boolean unique,
		NpcSpawnAttributes spawnAttributes
) {

	public static final Codec<NpcDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(NpcDefinition::id),
			Codec.STRING.optionalFieldOf("display_name").forGetter(definition -> optionalIfDefault(
					definition.displayName(),
					NpcDefinitionDefaults.DISPLAY_NAME
			)),
			NpcModelDefinition.CODEC.optionalFieldOf("model").forGetter(definition -> Optional.of(definition.model())),
			ResourceLocation.CODEC.optionalFieldOf("dialogue").forGetter(NpcDefinition::dialogue),
			ResourceLocation.CODEC.listOf().optionalFieldOf("factions").forGetter(definition -> optionalIfEmpty(definition.initialFactions())),
			Codec.STRING.listOf().optionalFieldOf("tags").forGetter(definition -> optionalIfEmpty(definition.tags())),
			Codec.BOOL.optionalFieldOf("no_ai").forGetter(definition -> optionalIfDefault(definition.noAi(), NpcDefinitionDefaults.NO_AI)),
			Codec.BOOL.optionalFieldOf("unique").forGetter(definition -> optionalIfDefault(definition.unique(), NpcDefinitionDefaults.UNIQUE)),
			NpcSpawnAttributes.CODEC.optionalFieldOf("attributes").forGetter(definition -> optionalIfEmpty(definition.spawnAttributes()))
	).apply(instance, NpcDefinitionResolver::resolve));

	private static Optional<String> optionalIfDefault(String value, String defaultValue) {
		return value.equals(defaultValue) ? Optional.empty() : Optional.of(value);
	}

	private static Optional<Boolean> optionalIfDefault(boolean value, boolean defaultValue) {
		return value == defaultValue ? Optional.empty() : Optional.of(value);
	}

	private static <T> Optional<List<T>> optionalIfEmpty(List<T> values) {
		return values.isEmpty() ? Optional.empty() : Optional.of(values);
	}

	private static Optional<NpcSpawnAttributes> optionalIfEmpty(NpcSpawnAttributes attributes) {
		return attributes.isEmpty() ? Optional.empty() : Optional.of(attributes);
	}
}
