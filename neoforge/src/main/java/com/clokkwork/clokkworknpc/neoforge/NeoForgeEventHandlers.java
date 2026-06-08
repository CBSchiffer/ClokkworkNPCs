package com.clokkwork.clokkworknpc.neoforge;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.command.ClokkworkNpcCommands;
import com.clokkwork.clokkworknpc.command.FactionCommands;
import com.clokkwork.clokkworknpc.data.load.ClokkworkNpcReloadListeners;
import com.clokkwork.clokkworknpc.faction.FactionWorldSync;
import com.clokkwork.clokkworknpc.npc.DialogueSessionManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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
		ClokkworkNpcCommands.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onServerStarted(ServerStartedEvent event) {
		FactionWorldSync.refreshLoadedWorlds(event.getServer());
	}

	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			DialogueSessionManager.endSession(serverPlayer);
		}
	}
}
