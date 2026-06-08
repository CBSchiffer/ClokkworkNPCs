package com.clokkwork.clokkworknpc.client;

import com.clokkwork.clokkworknpc.network.payload.ChooseDialogueOptionPayload;
import com.clokkwork.clokkworknpc.network.payload.CloseDialoguePayload;
import com.clokkwork.clokkworknpc.network.payload.DialogueSyncPayload;
import com.clokkwork.clokkworknpc.platform.Services;
import net.minecraft.client.Minecraft;

public final class DialogueClientHandlers {

	private DialogueClientHandlers() {
	}

	public static void handleDialogueSync(DialogueSyncPayload payload) {
		Minecraft.getInstance().execute(() -> {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.screen instanceof DialogueScreen dialogueScreen) {
				dialogueScreen.updateFromServer(payload);
			} else {
				minecraft.setScreen(new DialogueScreen(payload));
			}
		});
	}

	public static void handleCloseDialogue(CloseDialoguePayload payload) {
		Minecraft.getInstance().execute(() -> {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.screen instanceof DialogueScreen dialogueScreen) {
				dialogueScreen.closeFromServer();
			}
		});
	}

	public static void sendChooseOption(int optionIndex) {
		Services.DIALOGUE_NETWORKING.sendChooseOption(optionIndex);
	}

	public static void sendCancelDialogue() {
		sendChooseOption(ChooseDialogueOptionPayload.CANCEL_OPTION_INDEX);
	}
}
