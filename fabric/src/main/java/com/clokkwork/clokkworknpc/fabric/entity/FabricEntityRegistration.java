package com.clokkwork.clokkworknpc.fabric.entity;

import com.clokkwork.clokkworknpc.entity.ClokkworkNpcAttributes;
import com.clokkwork.clokkworknpc.entity.ClokkworkNpcEntityTypes;
import com.clokkwork.clokkworknpc.entity.GenericHumanoidNpcEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class FabricEntityRegistration {

	private FabricEntityRegistration() {
	}

	public static void register() {
		ResourceKey<EntityType<?>> key = ResourceKey.create(
				Registries.ENTITY_TYPE,
				ClokkworkNpcEntityTypes.GENERIC_HUMANOID_ID
		);
		EntityType<GenericHumanoidNpcEntity> entityType = Registry.register(
				BuiltInRegistries.ENTITY_TYPE,
				key,
				EntityType.Builder.of(GenericHumanoidNpcEntity::new, MobCategory.MISC)
						.sized(0.6F, 1.8F)
						.clientTrackingRange(10)
						.build(ClokkworkNpcEntityTypes.GENERIC_HUMANOID_ID.toString())
		);
		ClokkworkNpcEntityTypes.GENERIC_HUMANOID = entityType;
		FabricDefaultAttributeRegistry.register(entityType, ClokkworkNpcAttributes.genericHumanoid().build());
	}
}
