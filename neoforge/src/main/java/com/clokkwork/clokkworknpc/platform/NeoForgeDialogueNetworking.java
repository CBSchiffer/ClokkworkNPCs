package com.clokkwork.clokkworknpc.platform;

import com.clokkwork.clokkworknpc.client.DialogueClientHandlers;
import com.clokkwork.clokkworknpc.network.payload.ChooseDialogueOptionPayload;
import com.clokkwork.clokkworknpc.network.payload.CloseDialoguePayload;
import com.clokkwork.clokkworknpc.network.payload.DialogueSyncPayload;
import com.clokkwork.clokkworknpc.npc.DialogueNavigationService;
import com.clokkwork.clokkworknpc.platform.services.DialogueNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NeoForgeDialogueNetworking implements DialogueNetworking {

	public static void register(PayloadRegistrar registrar) {
		registrar.playToClient(DialogueSyncPayload.TYPE, DialogueSyncPayload.STREAM_CODEC, NeoForgeDialogueNetworking::handleDialogueSync);
		registrar.playToClient(CloseDialoguePayload.TYPE, CloseDialoguePayload.STREAM_CODEC, NeoForgeDialogueNetworking::handleCloseDialogue);
		registrar.playToServer(ChooseDialogueOptionPayload.TYPE, ChooseDialogueOptionPayload.STREAM_CODEC, NeoForgeDialogueNetworking::handleChooseOption);
	}

	@Override
	public void register() {
		// NeoForge payload registration is invoked from RegisterPayloadHandlersEvent.
	}

	@Override
	public void sendDialogueSync(ServerPlayer player, DialogueSyncPayload payload) {
		PacketDistributor.sendToPlayer(player, payload);
	}

	@Override
	public void sendCloseDialogue(ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, CloseDialoguePayload.INSTANCE);
	}

	@Override
	public void sendChooseOption(int optionIndex) {
		PacketDistributor.sendToServer(new ChooseDialogueOptionPayload(optionIndex));
	}

	private static void handleDialogueSync(DialogueSyncPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> DialogueClientHandlers.handleDialogueSync(payload));
	}

	private static void handleCloseDialogue(CloseDialoguePayload payload, IPayloadContext context) {
		context.enqueueWork(() -> DialogueClientHandlers.handleCloseDialogue(payload));
	}

	private static void handleChooseOption(ChooseDialogueOptionPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				DialogueNavigationService.handleChooseOption(serverPlayer, payload);
			}
		});
	}
}
