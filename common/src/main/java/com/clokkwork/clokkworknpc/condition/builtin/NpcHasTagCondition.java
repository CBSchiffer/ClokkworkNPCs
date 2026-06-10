package com.clokkwork.clokkworknpc.condition.builtin;

import com.clokkwork.clokkworknpc.condition.NpcConditionContext;
import com.clokkwork.clokkworknpc.condition.NpcConditionDefinition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record NpcHasTagCondition(String tag) implements NpcConditionDefinition {

	public static final ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath("clokkworknpc", "npc_has_tag");

	public static final MapCodec<NpcHasTagCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("tag").forGetter(NpcHasTagCondition::tag)
	).apply(instance, NpcHasTagCondition::new));

	@Override
	public ResourceLocation type() {
		return TYPE;
	}

	@Override
	public boolean test(NpcConditionContext context) {
		return context.npcHost().getNpcTags().contains(tag);
	}
}
