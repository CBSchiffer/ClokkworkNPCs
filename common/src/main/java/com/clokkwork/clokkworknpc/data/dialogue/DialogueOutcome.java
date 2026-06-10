package com.clokkwork.clokkworknpc.data.dialogue;

import com.clokkwork.clokkworknpc.condition.NpcConditionDefinition;
import com.clokkwork.clokkworknpc.condition.NpcConditionTypeRegistry;
import com.clokkwork.clokkworknpc.effect.NpcEffectDefinition;
import com.clokkwork.clokkworknpc.effect.NpcEffectTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public record DialogueOutcome(
		Optional<List<NpcConditionDefinition>> conditions,
		Optional<Boolean> defaultOutcome,
		Optional<List<NpcEffectDefinition>> effects,
		Optional<String> next,
		Optional<String> action
) {

	public static final Codec<DialogueOutcome> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			NpcConditionTypeRegistry.CODEC.listOf().optionalFieldOf("if").forGetter(DialogueOutcome::conditions),
			Codec.BOOL.optionalFieldOf("default").forGetter(DialogueOutcome::defaultOutcome),
			NpcEffectTypeRegistry.CODEC.listOf().optionalFieldOf("effects").forGetter(DialogueOutcome::effects),
			Codec.STRING.optionalFieldOf("next").forGetter(DialogueOutcome::next),
			Codec.STRING.optionalFieldOf("action").forGetter(DialogueOutcome::action)
	).apply(instance, DialogueOutcome::new));

	public boolean isDefault() {
		return defaultOutcome.orElse(false);
	}
}
