package com.clokkwork.clokkworknpc.effect.builtin;

import com.clokkwork.clokkworknpc.effect.NpcEffectContext;
import com.clokkwork.clokkworknpc.effect.NpcEffectDefinition;
import com.clokkwork.clokkworknpc.effect.ReputationOperation;
import com.clokkwork.clokkworknpc.reputation.PlayerReputations;
import com.clokkwork.clokkworknpc.reputation.ReputationBounds;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record ReputationEffect(
		ReputationOperation operation,
		ResourceLocation faction,
		int value
) implements NpcEffectDefinition {

	public static final ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath("clokkworknpc", "reputation");

	public static final MapCodec<ReputationEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ReputationOperation.CODEC.fieldOf("operation").forGetter(ReputationEffect::operation),
			ResourceLocation.CODEC.fieldOf("faction").forGetter(ReputationEffect::faction),
			Codec.INT.fieldOf("value").forGetter(ReputationEffect::value)
	).apply(instance, ReputationEffect::new));

	@Override
	public ResourceLocation type() {
		return TYPE;
	}

	@Override
	public void apply(NpcEffectContext context) {
		var player = context.interactionContext().player();
		var level = context.interactionContext().level();
		switch (operation) {
			case ADD -> PlayerReputations.add(level, player.getUUID(), faction, value);
			case SET -> PlayerReputations.set(level, player.getUUID(), faction, value);
			case RESET -> PlayerReputations.reset(level, player.getUUID(), faction);
		}
	}
}
