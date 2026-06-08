package com.clokkwork.clokkworknpc.fabric;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.command.ClokkworkNpcCommands;
import com.clokkwork.clokkworknpc.command.FactionCommands;
import com.clokkwork.clokkworknpc.data.load.ClokkworkNpcReloadListeners;
import com.clokkwork.clokkworknpc.fabric.data.PreparableReloadListenerBridge;
import com.clokkwork.clokkworknpc.faction.FactionWorldSync;
import com.clokkwork.clokkworknpc.npc.DialogueSessionManager;
import com.clokkwork.clokkworknpc.platform.FabricServerAccess;
import com.clokkwork.clokkworknpc.platform.Services;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public final class FabricModEvents {

	private FabricModEvents() {
	}

	public static void register() {
		Services.DIALOGUE_NETWORKING.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			FactionCommands.register(dispatcher);
			ClokkworkNpcCommands.register(dispatcher);
		});

		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
				new PreparableReloadListenerBridge(
						ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "npc_definitions"),
						ClokkworkNpcReloadListeners.NPC_DEFINITIONS
				)
		);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
				new PreparableReloadListenerBridge(
						ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "dialogue_definitions"),
						ClokkworkNpcReloadListeners.DIALOGUES
				)
		);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
				new PreparableReloadListenerBridge(
						ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "faction_definitions"),
						ClokkworkNpcReloadListeners.FACTIONS
				)
		);

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			FabricServerAccess.setCurrentServer(server);
			FactionWorldSync.refreshLoadedWorlds(server);
		});
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> FabricServerAccess.setCurrentServer(null));
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> DialogueSessionManager.endSession(handler.player));
	}
}
