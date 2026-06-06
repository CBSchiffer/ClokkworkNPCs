package com.clokkwork.clokkworknpc.command;

import com.clokkwork.clokkworknpc.data.npc.NpcDefinition;
import com.clokkwork.clokkworknpc.npc.NpcSpawnService;
import com.clokkwork.clokkworknpc.registry.ClokkworkNpcRegistries;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public final class NpcCommandHandlers {

	private NpcCommandHandlers() {
	}

	public static int spawn(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSourceStack source = context.getSource();
		ResourceLocation npcId = ResourceLocationArgument.getId(context, "npc_id");
		ServerLevel level = source.getLevel();

		NpcDefinition definition = ClokkworkNpcRegistries.NPC_DEFINITIONS.get(npcId).orElse(null);
		if (definition == null) {
			source.sendFailure(Component.literal("Unknown NPC definition: " + npcId));
			return 0;
		}

		Vec3 position = source.getPosition();
		float yaw = source.getRotation().y;

		var result = NpcSpawnService.trySpawnFromDefinition(level, position, yaw, definition);
		if (!result.succeeded()) {
			source.sendFailure(result.failureMessage().orElseGet(() -> Component.literal("Failed to spawn NPC: " + npcId)));
			return 0;
		}

		source.sendSuccess(
				() -> Component.literal("Spawned NPC ")
						.withStyle(ChatFormatting.GREEN)
						.append(Component.literal(definition.displayName()).withStyle(ChatFormatting.YELLOW))
						.append(Component.literal(" (" + npcId + ")").withStyle(ChatFormatting.GRAY)),
				true
		);
		return 1;
	}
}
