package com.clokkwork.clokkworknpc.platform;

import com.clokkwork.clokkworknpc.platform.services.ServerAccess;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class NeoForgeServerAccess implements ServerAccess {

	@Override
	public MinecraftServer getCurrentServer() {
		return ServerLifecycleHooks.getCurrentServer();
	}
}
