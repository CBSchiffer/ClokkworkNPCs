package com.clokkwork.clokkworknpc.effect;

import com.clokkwork.clokkworknpc.Constants;

import java.util.List;
import java.util.Optional;

public final class NpcEffectExecutor {

	private NpcEffectExecutor() {
	}

	public static void executeAll(Optional<List<NpcEffectDefinition>> effects, NpcEffectContext context) {
		if (effects.isEmpty()) {
			return;
		}

		for (NpcEffectDefinition effect : effects.get()) {
			try {
				effect.apply(context);
			} catch (RuntimeException exception) {
				Constants.LOG.warn("Failed to apply NPC effect type '{}'", effect.type(), exception);
			}
		}
	}
}
