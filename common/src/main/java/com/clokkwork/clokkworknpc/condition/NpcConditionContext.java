package com.clokkwork.clokkworknpc.condition;

import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.npc.INpcHost;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record NpcConditionContext(
		ServerPlayer player,
		INpcHost npcHost,
		ServerLevel level,
		Optional<NpcDefinition> npcDefinition
) {
}
