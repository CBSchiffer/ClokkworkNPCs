package com.clokkwork.clokkworknpc.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public record DialogueNode(
		Optional<String> text,
		Optional<List<String>> randomText,
		Optional<List<DialogueOption>> options,
		Optional<String> action
) {

	public static final Codec<DialogueNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("text").forGetter(DialogueNode::text),
			Codec.STRING.listOf().optionalFieldOf("random_text").forGetter(DialogueNode::randomText),
			DialogueOption.CODEC.listOf().optionalFieldOf("options").forGetter(DialogueNode::options),
			Codec.STRING.optionalFieldOf("action").forGetter(DialogueNode::action)
	).apply(instance, DialogueNode::new));
}
