package com.clokkwork.clokkworknpc.effect;

import com.mojang.serialization.Codec;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ReputationOperation {
	ADD("add"),
	SET("set"),
	RESET("reset");

	private static final Map<String, ReputationOperation> BY_JSON_NAME = Stream.of(values())
			.collect(Collectors.toMap(operation -> operation.jsonName, Function.identity()));

	public static final Codec<ReputationOperation> CODEC = Codec.STRING.flatXmap(
			name -> Optional.ofNullable(BY_JSON_NAME.get(name.toLowerCase(Locale.ROOT)))
					.map(com.mojang.serialization.DataResult::success)
					.orElseGet(() -> com.mojang.serialization.DataResult.error(() -> "Unknown reputation operation: " + name)),
			operation -> com.mojang.serialization.DataResult.success(operation.jsonName)
	);

	private final String jsonName;

	ReputationOperation(String jsonName) {
		this.jsonName = jsonName;
	}

	public String jsonName() {
		return jsonName;
	}
}
