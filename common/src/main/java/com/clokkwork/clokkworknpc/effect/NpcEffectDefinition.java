package com.clokkwork.clokkworknpc.effect;

import net.minecraft.resources.ResourceLocation;

public interface NpcEffectDefinition {

	ResourceLocation type();

	void apply(NpcEffectContext context);
}
