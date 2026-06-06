package com.clokkwork.clokkworknpc.platform.services;

import com.clokkwork.clokkworknpc.registry.FactionRegistry;
import net.minecraft.server.level.ServerLevel;

/**
 * Loader-specific access to a world's unified {@link FactionRegistry}.
 */
public interface FactionRegistryAccess {

	FactionRegistry get(ServerLevel level);
}
