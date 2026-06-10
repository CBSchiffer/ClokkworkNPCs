package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.condition.NpcConditionContext;
import com.clokkwork.clokkworknpc.condition.NpcConditionEvaluator;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueDefinition;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueNode;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueOption;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.network.payload.ChooseDialogueOptionPayload;
import com.clokkwork.clokkworknpc.network.payload.DialogueOptionView;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public final class DialogueTextResolver {

	public static final String FALLBACK_CLOSE_LABEL = "Close";

	private DialogueTextResolver() {
	}

	public static String resolveNpcName(INpcHost host, NpcDefinition definition) {
		var customName = host.asLivingEntity().getCustomName();
		if (customName != null) {
			return customName.getString();
		}
		return definition.displayName();
	}

	public static String resolveNodeText(DialogueNode node, RandomSource random) {
		return node.text()
				.filter(text -> !text.isBlank())
				.or(() -> node.randomText()
						.filter(lines -> !lines.isEmpty())
						.map(lines -> lines.get(random.nextInt(lines.size()))))
				.orElse("(no text)");
	}

	public static List<DialogueOptionView> resolveVisibleOptions(DialogueNode node, NpcConditionContext context) {
		List<DialogueOptionView> options = new ArrayList<>();
		List<DialogueOption> nodeOptions = node.options().orElse(List.of());
		for (int index = 0; index < nodeOptions.size(); index++) {
			DialogueOption option = nodeOptions.get(index);
			if (isOptionVisible(option, context)) {
				options.add(new DialogueOptionView(index, option.label()));
			}
		}

		if (options.isEmpty()) {
			return List.of(new DialogueOptionView(
					ChooseDialogueOptionPayload.CANCEL_OPTION_INDEX,
					FALLBACK_CLOSE_LABEL
			));
		}
		return List.copyOf(options);
	}

	public static boolean isOptionVisible(DialogueOption option, NpcConditionContext context) {
		return NpcConditionEvaluator.evaluateAll(option.visibleIf(), context);
	}

	public static DialogueNode getNode(DialogueDefinition dialogue, String nodeId) {
		return dialogue.nodes().get(nodeId);
	}
}
