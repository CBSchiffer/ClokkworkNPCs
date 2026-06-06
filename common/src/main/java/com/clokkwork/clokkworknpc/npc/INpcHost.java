package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.faction.FactionMembership;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

/**
 * Abstraction for something that can act as a framework NPC, independent of concrete entity class.
 */
public interface INpcHost {

	Optional<ResourceLocation> getNpcDefinitionId();

	void setNpcDefinitionId(ResourceLocation id);

	FactionMembership getFactionMembership();

	List<String> getNpcTags();

	ResourceLocation getSkinTexture();

	/** Applies spawn-template values from a definition to this entity instance. */
	void applyDefinition(NpcDefinition definition);

	LivingEntity asLivingEntity();
}
