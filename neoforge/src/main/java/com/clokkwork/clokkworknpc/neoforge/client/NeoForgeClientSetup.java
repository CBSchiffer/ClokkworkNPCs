package com.clokkwork.clokkworknpc.neoforge.client;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.neoforge.entity.NeoForgeEntityRegistration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class NeoForgeClientSetup {

	private NeoForgeClientSetup() {
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(
				NeoForgeEntityRegistration.GENERIC_HUMANOID.get(),
				GenericHumanoidNpcRenderer::new
		);
	}
}
