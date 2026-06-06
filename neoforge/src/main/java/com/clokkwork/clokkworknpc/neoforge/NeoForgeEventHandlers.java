package com.clokkwork.clokkworknpc.neoforge;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.command.FactionCommands;
import com.clokkwork.clokkworknpc.data.load.ClokkworkNpcReloadListeners;
import com.clokkwork.clokkworknpc.faction.FactionWorldSync;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class NeoForgeEventHandlers {

	private NeoForgeEventHandlers() {
	}

	@SubscribeEvent
	public static void onAddReloadListeners(AddReloadListenerEvent event) {
		event.addListener(ClokkworkNpcReloadListeners.NPC_DEFINITIONS);
		event.addListener(ClokkworkNpcReloadListeners.DIALOGUES);
		event.addListener(ClokkworkNpcReloadListeners.FACTIONS);
	}

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		FactionCommands.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onServerStarted(ServerStartedEvent event) {
		FactionWorldSync.refreshLoadedWorlds(event.getServer());
	}
}
