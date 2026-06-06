package com.clokkwork.clokkworknpc.faction;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.data.faction.FactionDataFiles;
import com.clokkwork.clokkworknpc.data.faction.FactionDefinition;
import com.clokkwork.clokkworknpc.registry.FactionRegistry;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * Per-world faction state: a unified {@link FactionRegistry} plus persistence for world-created factions.
 */
public final class WorldFactionSavedData extends SavedData {

	private static final String DATA_ID = Constants.MOD_ID + "_factions";
	private static final String FACTIONS_TAG = "world_factions";

	public static final SavedData.Factory<WorldFactionSavedData> FACTORY = new SavedData.Factory<>(
			WorldFactionSavedData::new,
			WorldFactionSavedData::load,
			DataFixTypes.LEVEL
	);

	private final FactionRegistry registry = new FactionRegistry(this::setDirty);
	private boolean initialized;

	private WorldFactionSavedData() {
	}

	public static WorldFactionSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
		WorldFactionSavedData data = new WorldFactionSavedData();
		Map<ResourceLocation, FactionDefinition> worldFactions = new HashMap<>();

		if (tag.contains(FACTIONS_TAG, Tag.TAG_COMPOUND)) {
			CompoundTag factionsTag = tag.getCompound(FACTIONS_TAG);
			DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
			for (String key : factionsTag.getAllKeys()) {
				ResourceLocation id = ResourceLocation.parse(key);
				FactionDefinition.CODEC.parse(ops, factionsTag.get(key))
						.resultOrPartial(message -> Constants.LOG.error("Failed to load world faction {}: {}", id, message))
						.ifPresent(definition -> worldFactions.put(id, definition));
			}
		}

		data.registry.initialize(FactionDataFiles.current(), worldFactions);
		data.initialized = true;
		return data;
	}

	public static WorldFactionSavedData get(ServerLevel level) {
		ServerLevel storageLevel = level.getServer().overworld();
		WorldFactionSavedData data = storageLevel.getDataStorage().computeIfAbsent(FACTORY, DATA_ID);
		data.ensureInitialized();
		return data;
	}

	private void ensureInitialized() {
		if (!initialized) {
			registry.initialize(FactionDataFiles.current(), Map.of());
			initialized = true;
		}
	}

	public FactionRegistry registry() {
		ensureInitialized();
		registry.syncDataFileFactions(FactionDataFiles.current());
		return registry;
	}

	public void syncDataFileFactions() {
		ensureInitialized();
		registry.syncDataFileFactions(FactionDataFiles.current());
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		CompoundTag factionsTag = new CompoundTag();
		DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);

		for (Map.Entry<ResourceLocation, FactionDefinition> entry : registry.exportWorldFactions().entrySet()) {
			FactionDefinition.CODEC.encodeStart(ops, entry.getValue())
					.resultOrPartial(message -> Constants.LOG.error("Failed to save world faction {}: {}", entry.getKey(), message))
					.ifPresent(encoded -> factionsTag.put(entry.getKey().toString(), encoded));
		}

		tag.put(FACTIONS_TAG, factionsTag);
		return tag;
	}
}
