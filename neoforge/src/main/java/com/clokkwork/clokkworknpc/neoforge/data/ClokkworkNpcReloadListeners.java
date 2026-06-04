package com.clokkwork.clokkworknpc.neoforge.data;

import com.clokkwork.clokkworknpc.data.DefinitionPaths;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueDefinition;
import com.clokkwork.clokkworknpc.data.load.JsonDefinitionReloadListener;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.registry.ClokkworkNpcRegistries;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public final class ClokkworkNpcReloadListeners {

	public static final PreparableReloadListener NPC_DEFINITIONS = new JsonDefinitionReloadListener<>(
			DefinitionPaths.NPCS,
			NpcDefinition.CODEC,
			NpcDefinition::id,
			ClokkworkNpcRegistries.NPC_DEFINITIONS::replaceAll,
			"NPC"
	);

	public static final PreparableReloadListener DIALOGUES = new JsonDefinitionReloadListener<>(
			DefinitionPaths.DIALOGUE,
			DialogueDefinition.CODEC,
			DialogueDefinition::id,
			ClokkworkNpcRegistries.DIALOGUES::replaceAll,
			"dialogue"
	);

	private ClokkworkNpcReloadListeners() {
	}
}
