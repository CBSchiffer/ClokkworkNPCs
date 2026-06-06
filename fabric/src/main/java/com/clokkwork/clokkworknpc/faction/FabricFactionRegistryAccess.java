package com.clokkwork.clokkworknpc.faction;

import com.clokkwork.clokkworknpc.data.faction.FactionDataFiles;
import com.clokkwork.clokkworknpc.platform.services.FactionRegistryAccess;
import com.clokkwork.clokkworknpc.registry.FactionRegistry;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * In-memory unified registry per level until Fabric world persistence is implemented.
 */
public final class FabricFactionRegistryAccess implements FactionRegistryAccess {

	private final Map<ServerLevel, FactionRegistry> registries = new WeakHashMap<>();

	@Override
	public FactionRegistry get(ServerLevel level) {
		return registries.computeIfAbsent(level, ignored -> {
			FactionRegistry registry = new FactionRegistry();
			registry.initialize(FactionDataFiles.current(), Map.of());
			return registry;
		});
	}
}
