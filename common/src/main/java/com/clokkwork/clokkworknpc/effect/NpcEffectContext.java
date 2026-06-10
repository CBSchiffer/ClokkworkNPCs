package com.clokkwork.clokkworknpc.effect;

import com.clokkwork.clokkworknpc.condition.NpcConditionContext;

public record NpcEffectContext(
		NpcConditionContext interactionContext
) {
}
