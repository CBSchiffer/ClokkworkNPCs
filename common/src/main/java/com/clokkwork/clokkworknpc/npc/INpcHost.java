package com.clokkwork.clokkworknpc.npc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

/**
 * Abstraction for something that can act as a framework NPC, independent of concrete entity class.
 */
public interface INpcHost {

	Optional<ResourceLocation> getNpcDefinitionId();

	void setNpcDefinitionId(ResourceLocation id);

	LivingEntity asLivingEntity();
}
