package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.condition.NpcConditionContext;
import com.clokkwork.clokkworknpc.condition.NpcConditionEvaluator;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueOption;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueOutcome;

import java.util.List;
import java.util.Optional;

public final class DialogueOutcomeResolver {

	private DialogueOutcomeResolver() {
	}

	public static ResolvedDialogueChoice resolve(DialogueOption option, NpcConditionContext context) {
		Optional<List<DialogueOutcome>> outcomes = option.outcomes();
		if (outcomes.isEmpty() || outcomes.get().isEmpty()) {
			return ResolvedDialogueChoice.fromOption(option);
		}

		DialogueOutcome defaultOutcome = null;
		for (DialogueOutcome outcome : outcomes.get()) {
			if (outcome.isDefault()) {
				defaultOutcome = outcome;
				continue;
			}
			if (NpcConditionEvaluator.evaluateAll(outcome.conditions(), context)) {
				return ResolvedDialogueChoice.fromOutcome(outcome);
			}
		}

		if (defaultOutcome != null) {
			return ResolvedDialogueChoice.fromOutcome(defaultOutcome);
		}

		return ResolvedDialogueChoice.fromOption(option);
	}
}
