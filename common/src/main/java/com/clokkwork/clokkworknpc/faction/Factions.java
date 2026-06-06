package com.clokkwork.clokkworknpc.faction;

import com.clokkwork.clokkworknpc.platform.Services;
import com.clokkwork.clokkworknpc.platform.services.FactionRegistryAccess;
import com.clokkwork.clokkworknpc.registry.FactionRegistry;
import net.minecraft.server.level.ServerLevel;

public final class Factions {

	private static final FactionRegistryAccess ACCESS = Services.load(FactionRegistryAccess.class);

	private Factions() {
	}

	public static FactionRegistry get(ServerLevel level) {
		return ACCESS.get(level);
	}
}
