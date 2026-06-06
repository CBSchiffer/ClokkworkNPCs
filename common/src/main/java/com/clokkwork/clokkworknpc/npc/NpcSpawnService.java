package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.entity.ClokkworkNpcEntityTypes;
import com.clokkwork.clokkworknpc.entity.GenericHumanoidNpcEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public final class NpcSpawnService {

	private NpcSpawnService() {
	}

	public static NpcSpawnResult trySpawnFromDefinition(
			ServerLevel level,
			Vec3 position,
			float yaw,
			NpcDefinition definition
	) {
		if (definition.unique() && WorldUniqueNpcSpawnData.get(level).hasBeenSpawned(definition.id())) {
			return NpcSpawnResult.failure(Component.literal(
					"This unique NPC has already been spawned in this world before: " + definition.id()
			));
		}

		if (ClokkworkNpcEntityTypes.GENERIC_HUMANOID == null) {
			Constants.LOG.error("Cannot spawn NPC {}: generic humanoid entity type is not registered", definition.id());
			return NpcSpawnResult.failure(Component.literal("NPC entity type is not registered"));
		}

		GenericHumanoidNpcEntity entity = new GenericHumanoidNpcEntity(ClokkworkNpcEntityTypes.GENERIC_HUMANOID, level);
		entity.moveTo(position.x, position.y, position.z, yaw, 0.0F);
		entity.applyDefinition(definition);

		if (!level.addFreshEntity(entity)) {
			Constants.LOG.warn("Failed to add NPC entity {} to level", definition.id());
			return NpcSpawnResult.failure(Component.literal("Failed to spawn NPC: " + definition.id()));
		}

		if (definition.unique()) {
			WorldUniqueNpcSpawnData.get(level).recordSpawn(definition.id());
		}

		return NpcSpawnResult.success(entity);
	}
}
