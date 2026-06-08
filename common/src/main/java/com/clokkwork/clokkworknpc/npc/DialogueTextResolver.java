package com.clokkwork.clokkworknpc.npc;

import com.clokkwork.clokkworknpc.data.dialogue.DialogueDefinition;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueNode;
import com.clokkwork.clokkworknpc.data.dialogue.DialogueOption;
import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.network.payload.DialogueOptionView;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public final class DialogueTextResolver {

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

	public static List<DialogueOptionView> resolveOptions(DialogueNode node) {
		List<DialogueOptionView> options = new ArrayList<>();
		List<DialogueOption> nodeOptions = node.options().orElse(List.of());
		for (int index = 0; index < nodeOptions.size(); index++) {
			options.add(new DialogueOptionView(index, nodeOptions.get(index).label()));
		}
		return List.copyOf(options);
	}

	public static DialogueNode getNode(DialogueDefinition dialogue, String nodeId) {
		return dialogue.nodes().get(nodeId);
	}
}
