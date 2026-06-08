package com.clokkwork.clokkworknpc.network;

import com.clokkwork.clokkworknpc.data.dialogue.DialogueDefinition;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueNode;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.npc.DialogueSession;
import com.clokkwork.clokkworknpc.npc.DialogueTextResolver;
import com.clokkwork.clokkworknpc.npc.INpcHost;
import com.clokkwork.clokkworknpc.network.payload.DialogueSyncPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public final class DialoguePayloadBuilder {

	private DialoguePayloadBuilder() {
	}

	public static DialogueSyncPayload buildSyncPayload(
			DialogueSession session,
			INpcHost host,
			NpcDefinition definition,
			DialogueDefinition dialogue,
			DialogueNode node
	) {
		return new DialogueSyncPayload(
				session.npcEntityId(),
				DialogueTextResolver.resolveNpcName(host, definition),
				session.dialogueId(),
				session.currentNodeId(),
				DialogueTextResolver.resolveNodeText(node, host.asLivingEntity().getRandom()),
				DialogueTextResolver.resolveOptions(node)
		);
	}

	public static INpcHost findSessionHost(ServerPlayer player, DialogueSession session) {
		Entity entity = player.serverLevel().getEntity(session.npcEntityId());
		if (entity instanceof INpcHost host) {
			return host;
		}
		return null;
	}
}
