package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.data.dialogue.DialogueOption;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueOutcome;
import com.clokkwork.clokkworknpc.effect.NpcEffectDefinition;

import java.util.List;
import java.util.Optional;

public record ResolvedDialogueChoice(
		Optional<List<NpcEffectDefinition>> effects,
		Optional<String> next,
		Optional<String> action
) {

	public static ResolvedDialogueChoice fromOption(DialogueOption option) {
		return new ResolvedDialogueChoice(option.effects(), option.next(), option.action());
	}

	public static ResolvedDialogueChoice fromOutcome(DialogueOutcome outcome) {
		return new ResolvedDialogueChoice(outcome.effects(), outcome.next(), outcome.action());
	}
}
