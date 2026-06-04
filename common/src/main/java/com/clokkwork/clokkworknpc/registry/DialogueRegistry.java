package com.clokkwork.clokkworknpc.registry;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class DialogueRegistry {

	private final Map<ResourceLocation, DialogueDefinition> dialogues = new ConcurrentHashMap<>();

	public void replaceAll(Map<ResourceLocation, DialogueDefinition> loaded) {
		dialogues.clear();
		for (DialogueDefinition definition : loaded.values()) {
			validate(definition);
			dialogues.put(definition.id(), definition);
		}
	}

	private static void validate(DialogueDefinition definition) {
		if (!definition.nodes().containsKey(definition.startNode())) {
			Constants.LOG.warn(
					"Dialogue {} references missing start node '{}'",
					definition.id(),
					definition.startNode()
			);
		}
	}

	public Optional<DialogueDefinition> get(ResourceLocation id) {
		return Optional.ofNullable(dialogues.get(id));
	}

	public Collection<DialogueDefinition> getAll() {
		return Collections.unmodifiableCollection(dialogues.values());
	}

	public boolean isEmpty() {
		return dialogues.isEmpty();
	}
}
