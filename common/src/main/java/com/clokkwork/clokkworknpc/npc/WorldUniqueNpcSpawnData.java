package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;

/**
 * Tracks which unique NPC definition IDs have been spawned in a world.
 * A definition marked {@code unique} may only be spawned once per world, even if the entity later dies.
 */
public final class WorldUniqueNpcSpawnData extends SavedData {

	private static final String DATA_ID = Constants.MOD_ID + "_unique_npc_spawns";
	private static final String SPAWNED_IDS_TAG = "spawned_definition_ids";

	public static final SavedData.Factory<WorldUniqueNpcSpawnData> FACTORY = new SavedData.Factory<>(
			WorldUniqueNpcSpawnData::new,
			WorldUniqueNpcSpawnData::load,
			DataFixTypes.LEVEL
	);

	private final Set<ResourceLocation> spawnedDefinitionIds = new HashSet<>();

	private WorldUniqueNpcSpawnData() {
	}

	public static WorldUniqueNpcSpawnData load(CompoundTag tag, HolderLookup.Provider provider) {
		WorldUniqueNpcSpawnData data = new WorldUniqueNpcSpawnData();
		if (tag.contains(SPAWNED_IDS_TAG, Tag.TAG_LIST)) {
			ListTag list = tag.getList(SPAWNED_IDS_TAG, Tag.TAG_STRING);
			for (Tag entry : list) {
				data.spawnedDefinitionIds.add(ResourceLocation.parse(entry.getAsString()));
			}
		}
		return data;
	}

	public static WorldUniqueNpcSpawnData get(ServerLevel level) {
		ServerLevel storageLevel = level.getServer().overworld();
		return storageLevel.getDataStorage().computeIfAbsent(FACTORY, DATA_ID);
	}

	public boolean hasBeenSpawned(ResourceLocation definitionId) {
		return spawnedDefinitionIds.contains(definitionId);
	}

	public void recordSpawn(ResourceLocation definitionId) {
		if (spawnedDefinitionIds.add(definitionId)) {
			setDirty();
		}
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		ListTag list = new ListTag();
		for (ResourceLocation id : spawnedDefinitionIds) {
			list.add(StringTag.valueOf(id.toString()));
		}
		tag.put(SPAWNED_IDS_TAG, list);
		return tag;
	}
}
