package com.clokkwork.clokkworknpc.reputation;

import com.clokkwork.clokkworknpc.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Per-world player reputation with factions. Stored on the Overworld regardless of which dimension
 * triggered the access.
 */
public final class WorldPlayerReputationSavedData extends SavedData {

	private static final String DATA_ID = Constants.MOD_ID + "_player_reputation";
	private static final String PLAYERS_TAG = "players";
	private static final String FACTIONS_TAG = "factions";

	public static final SavedData.Factory<WorldPlayerReputationSavedData> FACTORY = new SavedData.Factory<>(
			WorldPlayerReputationSavedData::new,
			WorldPlayerReputationSavedData::load,
			DataFixTypes.LEVEL
	);

	private final Map<UUID, Map<ResourceLocation, Integer>> reputations = new HashMap<>();

	private WorldPlayerReputationSavedData() {
	}

	public static WorldPlayerReputationSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
		WorldPlayerReputationSavedData data = new WorldPlayerReputationSavedData();
		if (!tag.contains(PLAYERS_TAG, Tag.TAG_COMPOUND)) {
			return data;
		}

		CompoundTag playersTag = tag.getCompound(PLAYERS_TAG);
		for (String playerKey : playersTag.getAllKeys()) {
			UUID playerId = UUID.fromString(playerKey);
			CompoundTag factionsTag = playersTag.getCompound(playerKey);
			Map<ResourceLocation, Integer> factionReputations = new HashMap<>();
			for (String factionKey : factionsTag.getAllKeys()) {
				factionReputations.put(ResourceLocation.parse(factionKey), factionsTag.getInt(factionKey));
			}
			data.reputations.put(playerId, factionReputations);
		}
		return data;
	}

	public static WorldPlayerReputationSavedData get(ServerLevel level) {
		ServerLevel storageLevel = level.getServer().overworld();
		return storageLevel.getDataStorage().computeIfAbsent(FACTORY, DATA_ID);
	}

	public int getReputation(UUID playerId, ResourceLocation factionId) {
		return reputations.getOrDefault(playerId, Map.of()).getOrDefault(factionId, ReputationBounds.DEFAULT);
	}

	public int setReputation(UUID playerId, ResourceLocation factionId, int value) {
		int clamped = ReputationBounds.clamp(value);
		Map<ResourceLocation, Integer> playerReputations = reputations.computeIfAbsent(playerId, ignored -> new HashMap<>());
		playerReputations.put(factionId, clamped);
		setDirty();
		return clamped;
	}

	public int addReputation(UUID playerId, ResourceLocation factionId, int delta) {
		int current = getReputation(playerId, factionId);
		return setReputation(playerId, factionId, current + delta);
	}

	public int resetReputation(UUID playerId, ResourceLocation factionId) {
		Map<ResourceLocation, Integer> playerReputations = reputations.get(playerId);
		if (playerReputations != null) {
			playerReputations.remove(factionId);
			if (playerReputations.isEmpty()) {
				reputations.remove(playerId);
			}
			setDirty();
		}
		return ReputationBounds.DEFAULT;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		CompoundTag playersTag = new CompoundTag();
		for (Map.Entry<UUID, Map<ResourceLocation, Integer>> playerEntry : reputations.entrySet()) {
			CompoundTag factionsTag = new CompoundTag();
			for (Map.Entry<ResourceLocation, Integer> factionEntry : playerEntry.getValue().entrySet()) {
				factionsTag.putInt(factionEntry.getKey().toString(), factionEntry.getValue());
			}
			playersTag.put(playerEntry.getKey().toString(), factionsTag);
		}
		tag.put(PLAYERS_TAG, playersTag);
		return tag;
	}
}
