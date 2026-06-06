package com.clokkwork.clokkworknpc.platform;

import com.clokkwork.clokkworknpc.platform.services.ServerAccess;
import net.minecraft.server.MinecraftServer;

public final class FabricServerAccess implements ServerAccess {

	private static MinecraftServer currentServer;

	public static void setCurrentServer(MinecraftServer server) {
		currentServer = server;
	}

	@Override
	public MinecraftServer getCurrentServer() {
		return currentServer;
	}
}
