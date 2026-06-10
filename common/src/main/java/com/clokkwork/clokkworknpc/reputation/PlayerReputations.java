package com.clokkwork.clokkworknpc.reputation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class PlayerReputations {

	private PlayerReputations() {
	}

	public static int get(ServerLevel level, UUID playerId, ResourceLocation factionId) {
		return WorldPlayerReputationSavedData.get(level).getReputation(playerId, factionId);
	}

	public static int get(ServerLevel level, ServerPlayer player, ResourceLocation factionId) {
		return get(level, player.getUUID(), factionId);
	}

	public static int set(ServerLevel level, UUID playerId, ResourceLocation factionId, int value) {
		return WorldPlayerReputationSavedData.get(level).setReputation(playerId, factionId, value);
	}

	public static int add(ServerLevel level, UUID playerId, ResourceLocation factionId, int delta) {
		return WorldPlayerReputationSavedData.get(level).addReputation(playerId, factionId, delta);
	}

	public static int reset(ServerLevel level, UUID playerId, ResourceLocation factionId) {
		return WorldPlayerReputationSavedData.get(level).resetReputation(playerId, factionId);
	}
}
