package com.clokkwork.clokkworknpc.condition;

import net.minecraft.resources.ResourceLocation;

public interface NpcConditionDefinition {

	ResourceLocation type();

	boolean test(NpcConditionContext context);
}
