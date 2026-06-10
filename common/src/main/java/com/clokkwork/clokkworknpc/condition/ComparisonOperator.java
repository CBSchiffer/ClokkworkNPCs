package com.clokkwork.clokkworknpc.condition;

import com.clokkwork.clokkworknpc.Constants;
import com.mojang.serialization.Codec;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ComparisonOperator {
	GREATER_THAN("greater_than"),
	GREATER_THAN_OR_EQUAL("greater_than_or_equal"),
	EQUAL("equal"),
	NOT_EQUAL("not_equal"),
	LESS_THAN_OR_EQUAL("less_than_or_equal"),
	LESS_THAN("less_than");

	private static final Map<String, ComparisonOperator> BY_JSON_NAME = Stream.of(values())
			.collect(Collectors.toMap(operator -> operator.jsonName, Function.identity()));

	public static final Codec<ComparisonOperator> CODEC = Codec.STRING.flatXmap(
			name -> Optional.ofNullable(BY_JSON_NAME.get(name.toLowerCase(Locale.ROOT)))
					.map(com.mojang.serialization.DataResult::success)
					.orElseGet(() -> com.mojang.serialization.DataResult.error(() -> "Unknown comparison operator: " + name)),
			operator -> com.mojang.serialization.DataResult.success(operator.jsonName)
	);

	private final String jsonName;

	ComparisonOperator(String jsonName) {
		this.jsonName = jsonName;
	}

	public boolean compare(int actual, int expected) {
		return switch (this) {
			case GREATER_THAN -> actual > expected;
			case GREATER_THAN_OR_EQUAL -> actual >= expected;
			case EQUAL -> actual == expected;
			case NOT_EQUAL -> actual != expected;
			case LESS_THAN_OR_EQUAL -> actual <= expected;
			case LESS_THAN -> actual < expected;
		};
	}
}
