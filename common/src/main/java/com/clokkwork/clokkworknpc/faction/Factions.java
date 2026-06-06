package com.clokkwork.clokkworknpc.faction;

import com.clokkwork.clokkworknpc.registry.FactionRegistry;
import net.minecraft.server.level.ServerLevel;

public final class Factions {

	private Factions() {
	}

	public static FactionRegistry get(ServerLevel level) {
		return WorldFactionSavedData.get(level).registry();
	}
}
