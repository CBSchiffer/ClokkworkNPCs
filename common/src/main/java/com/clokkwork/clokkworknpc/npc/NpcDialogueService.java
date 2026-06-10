package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueDefinition;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueNode;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.network.DialoguePayloadBuilder;
import com.clokkwork.clokkworknpc.platform.Services;
import com.clokkwork.clokkworknpc.registry.ClokkworkNpcRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public final class NpcDialogueService {

	private NpcDialogueService() {
	}

	public static InteractionResult tryStartDialogue(Player player, INpcHost host) {
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return InteractionResult.SUCCESS;
		}

		var definitionId = host.getNpcDefinitionId();
		if (definitionId.isEmpty()) {
			player.sendSystemMessage(Component.literal("This NPC has no definition and cannot start dialogue."));
			return InteractionResult.FAIL;
		}

		var definition = ClokkworkNpcRegistries.NPC_DEFINITIONS.get(definitionId.get());
		if (definition.isEmpty()) {
			Constants.LOG.warn(
					"NPC entity at {} references missing definition {}",
					host.asLivingEntity().blockPosition(),
					definitionId.get()
			);
			player.sendSystemMessage(Component.literal("This NPC's definition could not be loaded: " + definitionId.get()));
			return InteractionResult.FAIL;
		}

		var dialogueId = definition.get().dialogue();
		if (dialogueId.isEmpty()) {
			player.sendSystemMessage(Component.literal("This NPC has no dialogue configured."));
			return InteractionResult.FAIL;
		}

		var dialogue = ClokkworkNpcRegistries.DIALOGUES.get(dialogueId.get());
		if (dialogue.isEmpty()) {
			Constants.LOG.warn("NPC {} references missing dialogue {}", definitionId.get(), dialogueId.get());
			player.sendSystemMessage(Component.literal("This NPC's dialogue could not be loaded: " + dialogueId.get()));
			return InteractionResult.FAIL;
		}

		return startDialogue(serverPlayer, host, definition.get(), dialogue.get());
	}

	private static InteractionResult startDialogue(
			ServerPlayer player,
			INpcHost host,
			NpcDefinition definition,
			DialogueDefinition dialogue
	) {
		DialogueNode startNode = dialogue.getStartNode();
		if (startNode == null) {
			Constants.LOG.warn("Dialogue {} references missing start node '{}'", dialogue.id(), dialogue.startNode());
			player.sendSystemMessage(Component.literal("Dialogue start node is missing: " + dialogue.startNode()));
			return InteractionResult.FAIL;
		}

		DialogueSession session = DialogueSessionManager.startSession(
				player,
				host.asLivingEntity().getId(),
				dialogue.id(),
				dialogue.startNode()
		);
		Services.DIALOGUE_NETWORKING.sendDialogueSync(
				player,
				DialoguePayloadBuilder.buildSyncPayload(session, player, host, definition, dialogue, startNode)
		);
		return InteractionResult.sidedSuccess(player.level().isClientSide());
	}
}
