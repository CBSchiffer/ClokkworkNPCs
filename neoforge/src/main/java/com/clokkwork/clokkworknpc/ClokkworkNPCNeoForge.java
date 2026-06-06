package com.clokkwork.clokkworknpc;

import com.clokkwork.clokkworknpc.neoforge.entity.NeoForgeEntityRegistration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ClokkworkNPCNeoForge {

    public ClokkworkNPCNeoForge(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        NeoForgeEntityRegistration.register(eventBus);
        CommonClass.init();
    }
}