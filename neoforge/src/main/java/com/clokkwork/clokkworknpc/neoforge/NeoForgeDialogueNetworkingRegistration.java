package com.clokkwork.clokkworknpc.neoforge;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.platform.NeoForgeDialogueNetworking;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class NeoForgeDialogueNetworkingRegistration {

	private NeoForgeDialogueNetworkingRegistration() {
	}

	@SubscribeEvent
	public static void registerPayloads(RegisterPayloadHandlersEvent event) {
		NeoForgeDialogueNetworking.register(event.registrar("1"));
	}
}
