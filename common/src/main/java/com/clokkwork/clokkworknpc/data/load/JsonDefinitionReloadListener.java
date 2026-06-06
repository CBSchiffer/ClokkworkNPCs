package com.clokkwork.clokkworknpc.data.load;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.data.DefinitionCodecUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Loads JSON definition files from {@code data/<namespace>/<directory>/} into a registry.
 */
public final class JsonDefinitionReloadListener<T> extends SimpleJsonResourceReloadListener {

	private static final Gson GSON = new Gson();

	private final Codec<T> codec;
	private final Function<T, ResourceLocation> idGetter;
	private final Consumer<Map<ResourceLocation, T>> registryUpdater;
	private final String typeName;
	private final Runnable afterApply;

	public JsonDefinitionReloadListener(
			String directory,
			Codec<T> codec,
			Function<T, ResourceLocation> idGetter,
			Consumer<Map<ResourceLocation, T>> registryUpdater,
			String typeName
	) {
		this(directory, codec, idGetter, registryUpdater, typeName, null);
	}

	public JsonDefinitionReloadListener(
			String directory,
			Codec<T> codec,
			Function<T, ResourceLocation> idGetter,
			Consumer<Map<ResourceLocation, T>> registryUpdater,
			String typeName,
			Runnable afterApply
	) {
		super(GSON, directory);
		this.codec = codec;
		this.idGetter = idGetter;
		this.registryUpdater = registryUpdater;
		this.typeName = typeName;
		this.afterApply = afterApply;
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
		Map<ResourceLocation, T> loaded = new HashMap<>();

		for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
			ResourceLocation fileId = entry.getKey();
			DefinitionCodecUtil.parse(fileId, codec, entry.getValue()).ifPresent(definition -> {
				ResourceLocation definitionId = idGetter.apply(definition);
				if (!definitionId.equals(fileId)) {
					Constants.LOG.warn(
							"{} file {} declares id {}; using declared id for registry",
							typeName,
							fileId,
							definitionId
					);
				}
				if (loaded.put(definitionId, definition) != null) {
					Constants.LOG.error("Duplicate {} id {} while loading {}", typeName, definitionId, fileId);
				}
			});
		}

		registryUpdater.accept(Map.copyOf(loaded));
		Constants.LOG.info("Loaded {} {} definition(s)", loaded.size(), typeName);
		if (afterApply != null) {
			afterApply.run();
		}
	}
}
