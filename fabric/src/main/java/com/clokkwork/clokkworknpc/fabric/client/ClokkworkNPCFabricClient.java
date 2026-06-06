package com.clokkwork.clokkworknpc.fabric.client;

import com.clokkwork.clokkworknpc.entity.ClokkworkNpcEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ClokkworkNPCFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(
				ClokkworkNpcEntityTypes.GENERIC_HUMANOID,
				GenericHumanoidNpcRenderer::new
		);
	}
}
