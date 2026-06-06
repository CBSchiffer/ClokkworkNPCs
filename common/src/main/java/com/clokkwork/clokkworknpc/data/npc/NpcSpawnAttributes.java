package com.clokkwork.clokkworknpc.data.npc;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * Optional attribute overrides applied when an NPC is spawned from a definition.
 * Keys use vanilla attribute IDs (e.g. {@code minecraft:generic.max_health}).
 */
public record NpcSpawnAttributes(Map<ResourceLocation, Double> values) {

	public static final NpcSpawnAttributes EMPTY = new NpcSpawnAttributes(Map.of());

	public static final Codec<NpcSpawnAttributes> CODEC = Codec.unboundedMap(
			ResourceLocation.CODEC,
			Codec.DOUBLE
	).xmap(NpcSpawnAttributes::new, NpcSpawnAttributes::values);

	public boolean isEmpty() {
		return values.isEmpty();
	}
}
