package com.clokkwork.clokkworknpc.npc;

import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class DialogueSessionManager {

	private static final Map<UUID, DialogueSession> SESSIONS = new ConcurrentHashMap<>();

	private DialogueSessionManager() {
	}

	public static DialogueSession startSession(
			ServerPlayer player,
			int npcEntityId,
			net.minecraft.resources.ResourceLocation dialogueId,
			String startNodeId
	) {
		DialogueSession session = new DialogueSession(
				player.getUUID(),
				npcEntityId,
				dialogueId,
				startNodeId
		);
		SESSIONS.put(player.getUUID(), session);
		return session;
	}

	public static Optional<DialogueSession> getSession(ServerPlayer player) {
		return Optional.ofNullable(SESSIONS.get(player.getUUID()));
	}

	public static void updateNode(ServerPlayer player, String nodeId) {
		DialogueSession current = SESSIONS.get(player.getUUID());
		if (current == null) {
			return;
		}
		SESSIONS.put(player.getUUID(), new DialogueSession(
				current.playerId(),
				current.npcEntityId(),
				current.dialogueId(),
				nodeId
		));
	}

	public static void endSession(ServerPlayer player) {
		SESSIONS.remove(player.getUUID());
	}
}
