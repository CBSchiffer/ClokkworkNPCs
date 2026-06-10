package com.clokkwork.clokkworknpc.condition;

import com.clokkwork.clokkworknpc.Constants;

import java.util.List;
import java.util.Optional;

public final class NpcConditionEvaluator {

	private NpcConditionEvaluator() {
	}

	public static boolean evaluateAll(Optional<List<NpcConditionDefinition>> conditions, NpcConditionContext context) {
		if (conditions.isEmpty() || conditions.get().isEmpty()) {
			return true;
		}

		for (NpcConditionDefinition condition : conditions.get()) {
			try {
				if (!condition.test(context)) {
					return false;
				}
			} catch (RuntimeException exception) {
				Constants.LOG.warn("Failed to evaluate NPC condition type '{}'", condition.type(), exception);
				return false;
			}
		}
		return true;
	}
}
