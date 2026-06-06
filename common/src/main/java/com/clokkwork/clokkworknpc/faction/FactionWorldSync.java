package com.clokkwork.clokkworknpc.faction;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class FactionWorldSync {

	private FactionWorldSync() {
	}

	public static void refreshLoadedWorlds(MinecraftServer server) {
		if (server == null) {
			return;
		}
		for (ServerLevel level : server.getAllLevels()) {
			WorldFactionSavedData.get(level).syncDataFileFactions();
		}
	}
}
