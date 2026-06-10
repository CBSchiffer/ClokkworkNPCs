package com.clokkwork.clokkworknpc.condition;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.condition.builtin.FactionReputationCondition;
import com.clokkwork.clokkworknpc.condition.builtin.NpcHasTagCondition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class NpcConditionTypeRegistry {

	private static final Map<ResourceLocation, MapCodec<? extends NpcConditionDefinition>> CODECS = new HashMap<>();

	public static final Codec<NpcConditionDefinition> CODEC = ResourceLocation.CODEC.dispatch(
			"type",
			NpcConditionDefinition::type,
			NpcConditionTypeRegistry::codec
	);

	static {
		register(FactionReputationCondition.TYPE, FactionReputationCondition.CODEC);
		register(NpcHasTagCondition.TYPE, NpcHasTagCondition.CODEC);
	}

	private NpcConditionTypeRegistry() {
	}

	public static void register(ResourceLocation type, MapCodec<? extends NpcConditionDefinition> codec) {
		CODECS.put(type, codec);
	}

	private static MapCodec<? extends NpcConditionDefinition> codec(ResourceLocation type) {
		MapCodec<? extends NpcConditionDefinition> codec = CODECS.get(type);
		if (codec != null) {
			return codec;
		}

		Constants.LOG.warn("Unknown NPC condition type '{}'; using failing placeholder codec", type);
		return failingCodec(type);
	}

	private static MapCodec<NpcConditionDefinition> failingCodec(ResourceLocation type) {
		return MapCodec.unit(new NpcConditionDefinition() {
			@Override
			public ResourceLocation type() {
				return type;
			}

			@Override
			public boolean test(NpcConditionContext context) {
				Constants.LOG.warn("Evaluating unknown NPC condition type '{}'; treating as failed", type);
				return false;
			}
		});
	}
}
