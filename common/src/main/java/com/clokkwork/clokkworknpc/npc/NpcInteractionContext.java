package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.condition.NpcConditionContext;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import net.minecraft.server.level.ServerPlayer;

public final class NpcInteractionContext {

	private NpcInteractionContext() {
	}

	public static NpcConditionContext create(ServerPlayer player, INpcHost host, NpcDefinition definition) {
		return new NpcConditionContext(
				player,
				host,
				player.serverLevel(),
				java.util.Optional.of(definition)
		);
	}
}
