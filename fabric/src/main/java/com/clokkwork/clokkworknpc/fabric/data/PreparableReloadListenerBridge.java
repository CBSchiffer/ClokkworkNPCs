package com.clokkwork.clokkworknpc.fabric.data;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public record PreparableReloadListenerBridge(
		ResourceLocation id,
		PreparableReloadListener delegate
) implements IdentifiableResourceReloadListener {

	@Override
	public ResourceLocation getFabricId() {
		return id;
	}

	@Override
	public CompletableFuture<Void> reload(
			PreparationBarrier barrier,
			ResourceManager resourceManager,
			ProfilerFiller prepareProfiler,
			ProfilerFiller applyProfiler,
			Executor prepareExecutor,
			Executor applyExecutor
	) {
		return delegate.reload(barrier, resourceManager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
	}
}
