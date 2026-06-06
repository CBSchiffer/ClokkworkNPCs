package com.clokkwork.clokkworknpc.neoforge.entity;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.entity.ClokkworkNpcAttributes;
import com.clokkwork.clokkworknpc.entity.ClokkworkNpcEntityTypes;
import com.clokkwork.clokkworknpc.entity.GenericHumanoidNpcEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeEntityRegistration {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
			DeferredRegister.create(Registries.ENTITY_TYPE, Constants.MOD_ID);

	public static final DeferredHolder<EntityType<?>, EntityType<GenericHumanoidNpcEntity>> GENERIC_HUMANOID =
			ENTITY_TYPES.register(
					"generic_humanoid",
					() -> EntityType.Builder.of(GenericHumanoidNpcEntity::new, MobCategory.MISC)
							.sized(0.6F, 1.8F)
							.clientTrackingRange(10)
							.build(ClokkworkNpcEntityTypes.GENERIC_HUMANOID_ID.toString())
			);

	private NeoForgeEntityRegistration() {
	}

	public static void register(IEventBus modBus) {
		ENTITY_TYPES.register(modBus);
	}

	@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
	public static final class AttributeHandler {

		private AttributeHandler() {
		}

		@SubscribeEvent
		public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			var entityType = GENERIC_HUMANOID.get();
			ClokkworkNpcEntityTypes.GENERIC_HUMANOID = entityType;
			event.put(entityType, ClokkworkNpcAttributes.genericHumanoid().build());
		}
	}
}
