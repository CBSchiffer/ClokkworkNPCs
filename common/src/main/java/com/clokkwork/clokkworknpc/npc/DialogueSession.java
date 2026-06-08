package com.clokkwork.clokkworknpc.npc;

import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

/**
 * Server-side dialogue state for one player interacting with one NPC.
 */
public record DialogueSession(
		UUID playerId,
		int npcEntityId,
		ResourceLocation dialogueId,
		String currentNodeId
) {
}
