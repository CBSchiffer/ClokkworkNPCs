package com.clokkwork.clokkworknpc.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record DialogueDefinition(ResourceLocation id, String startNode, Map<String, DialogueNode> nodes) {

	public static final Codec<DialogueDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(DialogueDefinition::id),
			Codec.STRING.fieldOf("start").forGetter(DialogueDefinition::startNode),
			Codec.unboundedMap(Codec.STRING, DialogueNode.CODEC).fieldOf("nodes").forGetter(DialogueDefinition::nodes)
	).apply(instance, DialogueDefinition::new));

	public DialogueNode getStartNode() {
		return nodes.get(startNode);
	}
}
