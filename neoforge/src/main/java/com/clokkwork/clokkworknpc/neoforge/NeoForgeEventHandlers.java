package com.clokkwork.clokkworknpc.neoforge;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.neoforge.data.ClokkworkNpcReloadListeners;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class NeoForgeEventHandlers {

	private NeoForgeEventHandlers() {
	}

	@SubscribeEvent
	public static void onAddReloadListeners(AddReloadListenerEvent event) {
		event.addListener(ClokkworkNpcReloadListeners.NPC_DEFINITIONS);
		event.addListener(ClokkworkNpcReloadListeners.DIALOGUES);
		event.addListener(ClokkworkNpcReloadListeners.FACTIONS);
	}
}
