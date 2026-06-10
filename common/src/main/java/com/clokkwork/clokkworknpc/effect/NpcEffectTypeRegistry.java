package com.clokkwork.clokkworknpc.effect;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.effect.builtin.ReputationEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class NpcEffectTypeRegistry {

	private static final Map<ResourceLocation, MapCodec<? extends NpcEffectDefinition>> CODECS = new HashMap<>();

	public static final Codec<NpcEffectDefinition> CODEC = ResourceLocation.CODEC.dispatch(
			"type",
			NpcEffectDefinition::type,
			NpcEffectTypeRegistry::codec
	);

	static {
		register(ReputationEffect.TYPE, ReputationEffect.CODEC);
	}

	private NpcEffectTypeRegistry() {
	}

	public static void register(ResourceLocation type, MapCodec<? extends NpcEffectDefinition> codec) {
		CODECS.put(type, codec);
	}

	private static MapCodec<? extends NpcEffectDefinition> codec(ResourceLocation type) {
		MapCodec<? extends NpcEffectDefinition> codec = CODECS.get(type);
		if (codec != null) {
			return codec;
		}

		Constants.LOG.warn("Unknown NPC effect type '{}'; using no-op placeholder codec", type);
		return noOpCodec(type);
	}

	private static MapCodec<NpcEffectDefinition> noOpCodec(ResourceLocation type) {
		return MapCodec.unit(new NpcEffectDefinition() {
			@Override
			public ResourceLocation type() {
				return type;
			}

			@Override
			public void apply(NpcEffectContext context) {
				Constants.LOG.warn("Applying unknown NPC effect type '{}'; skipping", type);
			}
		});
	}
}
