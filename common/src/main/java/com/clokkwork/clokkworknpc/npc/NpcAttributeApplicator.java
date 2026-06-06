package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.data.npc.NpcSpawnAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.Map;

public final class NpcAttributeApplicator {

	private NpcAttributeApplicator() {
	}

	public static void apply(LivingEntity entity, ResourceLocation definitionId, NpcSpawnAttributes attributes) {
		if (attributes.isEmpty()) {
			return;
		}

		for (Map.Entry<ResourceLocation, Double> entry : attributes.values().entrySet()) {
			ResourceLocation attributeId = entry.getKey();
			double value = entry.getValue();

			BuiltInRegistries.ATTRIBUTE.getHolder(attributeId).ifPresentOrElse(
					holder -> applyAttribute(entity, definitionId, attributeId, holder, value),
					() -> Constants.LOG.warn(
							"NPC definition {} references unknown attribute {}",
							definitionId,
							attributeId
					)
			);
		}
	}

	private static void applyAttribute(
			LivingEntity entity,
			ResourceLocation definitionId,
			ResourceLocation attributeId,
			Holder<Attribute> attribute,
			double value
	) {
		if (!entity.getAttributes().hasAttribute(attribute)) {
			Constants.LOG.warn(
					"NPC definition {} attribute {} is not supported by {}",
					definitionId,
					attributeId,
					entity.getType().getDescriptionId()
			);
			return;
		}

		AttributeInstance instance = entity.getAttribute(attribute);
		if (instance != null) {
			instance.setBaseValue(value);
		}
	}
}
