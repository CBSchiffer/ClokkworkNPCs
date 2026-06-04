package com.clokkwork.clokkworknpc.data;

import com.clokkwork.clokkworknpc.Constants;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public final class DefinitionCodecUtil {

	private DefinitionCodecUtil() {
	}

	public static <T> Optional<T> parse(ResourceLocation fileId, Codec<T> codec, JsonElement json) {
		DataResult<T> result = codec.parse(JsonOps.INSTANCE, json);
		return result.resultOrPartial(message -> Constants.LOG.error("Failed to parse definition {}: {}", fileId, message));
	}
}
