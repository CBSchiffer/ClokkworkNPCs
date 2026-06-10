package com.clokkwork.clokkworknpc.data.dialogue;

import com.clokkwork.clokkworknpc.condition.NpcConditionDefinition;
import com.clokkwork.clokkworknpc.condition.NpcConditionTypeRegistry;
import com.clokkwork.clokkworknpc.effect.NpcEffectDefinition;
import com.clokkwork.clokkworknpc.effect.NpcEffectTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public record DialogueOption(
		String label,
		Optional<List<NpcConditionDefinition>> visibleIf,
		Optional<List<NpcEffectDefinition>> effects,
		Optional<List<DialogueOutcome>> outcomes,
		Optional<String> next,
		Optional<String> action
) {

	public static final Codec<DialogueOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("label").forGetter(DialogueOption::label),
			NpcConditionTypeRegistry.CODEC.listOf().optionalFieldOf("visible_if").forGetter(DialogueOption::visibleIf),
			NpcEffectTypeRegistry.CODEC.listOf().optionalFieldOf("effects").forGetter(DialogueOption::effects),
			DialogueOutcome.CODEC.listOf().optionalFieldOf("outcomes").forGetter(DialogueOption::outcomes),
			Codec.STRING.optionalFieldOf("next").forGetter(DialogueOption::next),
			Codec.STRING.optionalFieldOf("action").forGetter(DialogueOption::action)
	).apply(instance, DialogueOption::new));
}
