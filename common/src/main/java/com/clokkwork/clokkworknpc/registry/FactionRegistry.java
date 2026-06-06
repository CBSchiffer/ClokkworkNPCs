package com.clokkwork.clokkworknpc.registry;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.data.faction.FactionDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Unified faction registry for a world. Data-file and world-created factions share one map
 * and are treated identically for lookup. Only world-created IDs are tracked for save export.
 */
public final class FactionRegistry {

	private final Map<ResourceLocation, FactionDefinition> factions = new ConcurrentHashMap<>();
	private final Set<ResourceLocation> worldCreatedIds = ConcurrentHashMap.newKeySet();
	private final Runnable onWorldFactionChanged;

	public FactionRegistry() {
		this(() -> {});
	}

	public FactionRegistry(Runnable onWorldFactionChanged) {
		this.onWorldFactionChanged = onWorldFactionChanged;
	}

	/**
	 * Builds the registry at world startup from data-file definitions and any persisted world factions.
	 */
	public void initialize(Map<ResourceLocation, FactionDefinition> dataFileFactions, Map<ResourceLocation, FactionDefinition> worldFactions) {
		factions.clear();
		worldCreatedIds.clear();
		factions.putAll(dataFileFactions);
		for (Map.Entry<ResourceLocation, FactionDefinition> entry : worldFactions.entrySet()) {
			factions.put(entry.getKey(), entry.getValue());
			worldCreatedIds.add(entry.getKey());
		}
	}

	/**
	 * Merges data-file factions into the registry. World-created entries win on ID conflicts.
	 */
	public void syncDataFileFactions(Map<ResourceLocation, FactionDefinition> dataFileFactions) {
		factions.entrySet().removeIf(entry -> !worldCreatedIds.contains(entry.getKey()));
		for (Map.Entry<ResourceLocation, FactionDefinition> entry : dataFileFactions.entrySet()) {
			if (worldCreatedIds.contains(entry.getKey())) {
				Constants.LOG.warn(
						"Skipping data-file faction {}; world-created faction already exists",
						entry.getKey()
				);
				continue;
			}
			factions.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Registers a world-created faction. Fails if any faction with the same ID already exists.
	 */
	public boolean register(FactionDefinition faction) {
		ResourceLocation id = faction.id();
		if (contains(id)) {
			return false;
		}
		factions.put(id, faction);
		worldCreatedIds.add(id);
		onWorldFactionChanged.run();
		return true;
	}

	public Map<ResourceLocation, FactionDefinition> exportWorldFactions() {
		Map<ResourceLocation, FactionDefinition> exported = new HashMap<>();
		for (ResourceLocation id : worldCreatedIds) {
			FactionDefinition definition = factions.get(id);
			if (definition != null) {
				exported.put(id, definition);
			}
		}
		return exported;
	}

	public Optional<FactionDefinition> get(ResourceLocation id) {
		return Optional.ofNullable(factions.get(id));
	}

	public Collection<FactionDefinition> getAll() {
		return Collections.unmodifiableCollection(factions.values());
	}

	public boolean contains(ResourceLocation id) {
		return factions.containsKey(id);
	}

	public boolean isEmpty() {
		return factions.isEmpty();
	}
}
