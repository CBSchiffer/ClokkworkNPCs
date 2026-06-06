package com.clokkwork.clokkworknpc.data.npc;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

/**
 * Applies find-or-default rules to parsed NPC definition JSON. Only {@code id} is required.
 */
public final class NpcDefinitionResolver {

	private NpcDefinitionResolver() {
	}

	public static NpcDefinition resolve(
			ResourceLocation id,
			Optional<String> displayName,
			Optional<NpcModelDefinition> model,
			Optional<ResourceLocation> dialogue,
			Optional<List<ResourceLocation>> initialFactions,
			Optional<List<String>> tags,
			Optional<Boolean> noAi,
			Optional<Boolean> unique,
			Optional<NpcSpawnAttributes> attributes
	) {
		return new NpcDefinition(
				id,
				resolveDisplayName(displayName),
				resolveModel(model),
				dialogue,
				initialFactions.orElseGet(List::of),
				tags.orElseGet(List::of),
				noAi.orElse(NpcDefinitionDefaults.NO_AI),
				unique.orElse(NpcDefinitionDefaults.UNIQUE),
				attributes.orElse(NpcSpawnAttributes.EMPTY)
		);
	}

	private static String resolveDisplayName(Optional<String> displayName) {
		return displayName
				.map(String::trim)
				.filter(name -> !name.isEmpty())
				.orElse(NpcDefinitionDefaults.DISPLAY_NAME);
	}

	public static NpcModelDefinition resolveModel(Optional<NpcModelDefinition> model) {
		if (model.isEmpty()) {
			return NpcModelDefinition.defaultHumanoid();
		}

		NpcModelDefinition parsed = model.get();
		ResourceLocation type = parsed.type();
		List<ResourceLocation> skins = parsed.skins();

		if (skins.isEmpty() && NpcDefinitionDefaults.HUMANOID_MODEL_TYPE.equals(type)) {
			return new NpcModelDefinition(type, List.of(NpcDefinitionDefaults.DEFAULT_STEVE_SKIN));
		}

		return new NpcModelDefinition(type, skins);
	}
}
