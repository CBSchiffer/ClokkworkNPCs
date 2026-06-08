package com.clokkwork.clokkworknpc.platform.services;

import com.clokkwork.clokkworknpc.network.payload.ChooseDialogueOptionPayload;
import com.clokkwork.clokkworknpc.network.payload.CloseDialoguePayload;
import com.clokkwork.clokkworknpc.network.payload.DialogueSyncPayload;
import net.minecraft.server.level.ServerPlayer;

public interface DialogueNetworking {

	void register();

	void sendDialogueSync(ServerPlayer player, DialogueSyncPayload payload);

	void sendCloseDialogue(ServerPlayer player);

	void sendChooseOption(int optionIndex);
}
