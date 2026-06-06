package com.clokkwork.clokkworknpc.faction;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mutable faction membership for a runtime NPC instance. Initialized from an NPC definition's
 * {@code factions} spawn field when the entity is created; may change during gameplay independently
 * of the definition template.
 */
public final class FactionMembership {

	private final Set<ResourceLocation> factions = new HashSet<>();

	public static FactionMembership fromInitial(List<ResourceLocation> initialFactions) {
		FactionMembership membership = new FactionMembership();
		membership.factions.addAll(initialFactions);
		return membership;
	}

	public Set<ResourceLocation> asSet() {
		return Collections.unmodifiableSet(factions);
	}

	public boolean belongsTo(ResourceLocation factionId) {
		return factions.contains(factionId);
	}

	public boolean add(ResourceLocation factionId) {
		return factions.add(factionId);
	}

	public boolean remove(ResourceLocation factionId) {
		return factions.remove(factionId);
	}

	public void replaceAll(Collection<ResourceLocation> factionIds) {
		factions.clear();
		factions.addAll(factionIds);
	}
}
