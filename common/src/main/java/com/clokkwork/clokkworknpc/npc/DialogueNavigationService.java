package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueDefinition;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueNode;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueOption;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.network.DialoguePayloadBuilder;
import com.clokkwork.clokkworknpc.network.payload.ChooseDialogueOptionPayload;
import com.clokkwork.clokkworknpc.platform.Services;
import com.clokkwork.clokkworknpc.registry.ClokkworkNpcRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class DialogueNavigationService {

	private DialogueNavigationService() {
	}

	public static void handleChooseOption(ServerPlayer player, ChooseDialogueOptionPayload payload) {
		if (payload.optionIndex() == ChooseDialogueOptionPayload.CANCEL_OPTION_INDEX) {
			closeDialogue(player);
			return;
		}

		var sessionOptional = DialogueSessionManager.getSession(player);
		if (sessionOptional.isEmpty()) {
			Constants.LOG.debug("Ignoring dialogue option from {} with no active session", player.getGameProfile().getName());
			return;
		}

		DialogueSession session = sessionOptional.get();
		var dialogueOptional = ClokkworkNpcRegistries.DIALOGUES.get(session.dialogueId());
		if (dialogueOptional.isEmpty()) {
			Constants.LOG.warn("Active dialogue session for {} references missing dialogue {}", player.getGameProfile().getName(), session.dialogueId());
			closeDialogue(player);
			return;
		}

		DialogueDefinition dialogue = dialogueOptional.get();
		DialogueNode currentNode = DialogueTextResolver.getNode(dialogue, session.currentNodeId());
		if (currentNode == null) {
			Constants.LOG.warn("Active dialogue session for {} references missing node {}", player.getGameProfile().getName(), session.currentNodeId());
			closeDialogue(player);
			return;
		}

		List<DialogueOption> options = currentNode.options().orElse(List.of());
		if (payload.optionIndex() < 0 || payload.optionIndex() >= options.size()) {
			player.sendSystemMessage(Component.literal("That dialogue option is no longer available."));
			closeDialogue(player);
			return;
		}

		DialogueOption selected = options.get(payload.optionIndex());
		if (selected.action().map("close"::equalsIgnoreCase).orElse(false)) {
			closeDialogue(player);
			return;
		}

		if (selected.next().isPresent()) {
			advanceToNode(player, session, dialogue, selected.next().get());
			return;
		}

		if (selected.action().isPresent()) {
			Constants.LOG.info(
					"Unsupported dialogue option action '{}' for player {}; closing session",
					selected.action().get(),
					player.getGameProfile().getName()
			);
		}
		closeDialogue(player);
	}

	private static void advanceToNode(
			ServerPlayer player,
			DialogueSession session,
			DialogueDefinition dialogue,
			String nextNodeId
	) {
		DialogueNode nextNode = DialogueTextResolver.getNode(dialogue, nextNodeId);
		if (nextNode == null) {
			Constants.LOG.warn("Dialogue {} missing node '{}' for player {}", dialogue.id(), nextNodeId, player.getGameProfile().getName());
			player.sendSystemMessage(Component.literal("Dialogue node is missing: " + nextNodeId));
			closeDialogue(player);
			return;
		}

		INpcHost host = DialoguePayloadBuilder.findSessionHost(player, session);
		if (host == null) {
			player.sendSystemMessage(Component.literal("That NPC is no longer available."));
			closeDialogue(player);
			return;
		}

		var definitionId = host.getNpcDefinitionId();
		if (definitionId.isEmpty()) {
			closeDialogue(player);
			return;
		}

		var definitionOptional = ClokkworkNpcRegistries.NPC_DEFINITIONS.get(definitionId.get());
		if (definitionOptional.isEmpty()) {
			closeDialogue(player);
			return;
		}

		NpcDefinition definition = definitionOptional.get();
		DialogueSessionManager.updateNode(player, nextNodeId);
		DialogueSession updatedSession = DialogueSessionManager.getSession(player).orElseThrow();
		Services.DIALOGUE_NETWORKING.sendDialogueSync(
				player,
				DialoguePayloadBuilder.buildSyncPayload(updatedSession, host, definition, dialogue, nextNode)
		);
	}

	public static void closeDialogue(ServerPlayer player) {
		DialogueSessionManager.endSession(player);
		Services.DIALOGUE_NETWORKING.sendCloseDialogue(player);
	}
}
