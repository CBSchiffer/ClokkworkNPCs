package com.clokkwork.clokkworknpc.registry;

import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class NpcDefinitionRegistry {

	private final Map<ResourceLocation, NpcDefinition> definitions = new ConcurrentHashMap<>();

	public void replaceAll(Map<ResourceLocation, NpcDefinition> loaded) {
		definitions.clear();
		definitions.putAll(loaded);
	}

	public Optional<NpcDefinition> get(ResourceLocation id) {
		return Optional.ofNullable(definitions.get(id));
	}

	public Collection<NpcDefinition> getAll() {
		return Collections.unmodifiableCollection(definitions.values());
	}

	public boolean isEmpty() {
		return definitions.isEmpty();
	}
}
