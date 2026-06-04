package com.clokkwork.clokkworknpc.registry;

/**
 * Runtime registries populated by JSON reload listeners on the server.
 */
public final class ClokkworkNpcRegistries {

	public static final NpcDefinitionRegistry NPC_DEFINITIONS = new NpcDefinitionRegistry();
	public static final DialogueRegistry DIALOGUES = new DialogueRegistry();

	private ClokkworkNpcRegistries() {
	}
}
