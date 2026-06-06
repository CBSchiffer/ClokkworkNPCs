package com.clokkwork.clokkworknpc.neoforge.faction;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class FactionWorldSync {

	private FactionWorldSync() {
	}

	public static void refreshLoadedWorlds() {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			return;
		}
		for (ServerLevel level : server.getAllLevels()) {
			WorldFactionSavedData.get(level).refreshDataFileFactions();
		}
	}
}
