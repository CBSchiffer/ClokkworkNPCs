package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.entity.GenericHumanoidNpcEntity;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public record NpcSpawnResult(Optional<GenericHumanoidNpcEntity> entity, Optional<Component> failureMessage) {

	public static NpcSpawnResult success(GenericHumanoidNpcEntity entity) {
		return new NpcSpawnResult(Optional.of(entity), Optional.empty());
	}

	public static NpcSpawnResult failure(Component message) {
		return new NpcSpawnResult(Optional.empty(), Optional.of(message));
	}

	public boolean succeeded() {
		return entity.isPresent();
	}
}
