package com.clokkwork.clokkworknpc.neoforge.faction;

import com.clokkwork.clokkworknpc.platform.services.FactionRegistryAccess;
import com.clokkwork.clokkworknpc.registry.FactionRegistry;
import net.minecraft.server.level.ServerLevel;

public final class NeoForgeFactionRegistryAccess implements FactionRegistryAccess {

	@Override
	public FactionRegistry get(ServerLevel level) {
		return WorldFactionSavedData.get(level).registry();
	}
}
