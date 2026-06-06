package com.clokkwork.clokkworknpc.data.faction;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * Latest faction definitions loaded from JSON data files. Merged into each world's
 * {@link com.clokkwork.clokkworknpc.registry.FactionRegistry} at startup and on reload.
 */
public final class FactionDataFiles {

	private static volatile Map<ResourceLocation, FactionDefinition> definitions = Map.of();

	private FactionDataFiles() {
	}

	public static void replaceAll(Map<ResourceLocation, FactionDefinition> loaded) {
		definitions = Map.copyOf(loaded);
	}

	public static Map<ResourceLocation, FactionDefinition> current() {
		return definitions;
	}
}
