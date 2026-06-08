package com.clokkwork.clokkworknpc.platform;

import com.clokkwork.clokkworknpc.client.DialogueClientHandlers;
import com.clokkwork.clokkworknpc.network.payload.ChooseDialogueOptionPayload;
import com.clokkwork.clokkworknpc.network.payload.CloseDialoguePayload;
import com.clokkwork.clokkworknpc.network.payload.DialogueSyncPayload;
import com.clokkwork.clokkworknpc.npc.DialogueNavigationService;
import com.clokkwork.clokkworknpc.platform.services.DialogueNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class FabricDialogueNetworking implements DialogueNetworking {

	@Override
	public void register() {
		PayloadTypeRegistry.playS2C().register(DialogueSyncPayload.TYPE, DialogueSyncPayload.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(CloseDialoguePayload.TYPE, CloseDialoguePayload.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(ChooseDialogueOptionPayload.TYPE, ChooseDialogueOptionPayload.STREAM_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(ChooseDialogueOptionPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> DialogueNavigationService.handleChooseOption(context.player(), payload));
		});
	}

	public static void registerClient() {
		ClientPlayNetworking.registerGlobalReceiver(DialogueSyncPayload.TYPE, (payload, context) -> {
			DialogueClientHandlers.handleDialogueSync(payload);
		});
		ClientPlayNetworking.registerGlobalReceiver(CloseDialoguePayload.TYPE, (payload, context) -> {
			DialogueClientHandlers.handleCloseDialogue(payload);
		});
	}

	@Override
	public void sendDialogueSync(ServerPlayer player, DialogueSyncPayload payload) {
		ServerPlayNetworking.send(player, payload);
	}

	@Override
	public void sendCloseDialogue(ServerPlayer player) {
		ServerPlayNetworking.send(player, CloseDialoguePayload.INSTANCE);
	}

	@Override
	public void sendChooseOption(int optionIndex) {
		ClientPlayNetworking.send(new ChooseDialogueOptionPayload(optionIndex));
	}
}
