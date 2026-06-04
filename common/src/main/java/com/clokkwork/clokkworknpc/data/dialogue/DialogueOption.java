package com.clokkwork.clokkworknpc.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record DialogueOption(String label, Optional<String> next, Optional<String> action) {

	public static final Codec<DialogueOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("label").forGetter(DialogueOption::label),
			Codec.STRING.optionalFieldOf("next").forGetter(DialogueOption::next),
			Codec.STRING.optionalFieldOf("action").forGetter(DialogueOption::action)
	).apply(instance, DialogueOption::new));
}
