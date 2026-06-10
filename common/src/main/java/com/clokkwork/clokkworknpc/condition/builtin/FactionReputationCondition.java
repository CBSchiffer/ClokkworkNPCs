package com.clokkwork.clokkworknpc.condition.builtin;

import com.clokkwork.clokkworknpc.condition.ComparisonOperator;
import com.clokkwork.clokkworknpc.condition.NpcConditionContext;
import com.clokkwork.clokkworknpc.condition.NpcConditionDefinition;
import com.clokkwork.clokkworknpc.reputation.PlayerReputations;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record FactionReputationCondition(
		ResourceLocation faction,
		ComparisonOperator comparison,
		int value
) implements NpcConditionDefinition {

	public static final ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath("clokkworknpc", "faction_reputation");

	public static final MapCodec<FactionReputationCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("faction").forGetter(FactionReputationCondition::faction),
			ComparisonOperator.CODEC.fieldOf("comparison").forGetter(FactionReputationCondition::comparison),
			Codec.INT.fieldOf("value").forGetter(FactionReputationCondition::value)
	).apply(instance, FactionReputationCondition::new));

	@Override
	public ResourceLocation type() {
		return TYPE;
	}

	@Override
	public boolean test(NpcConditionContext context) {
		int reputation = PlayerReputations.get(context.level(), context.player(), faction);
		return comparison.compare(reputation, value);
	}
}
