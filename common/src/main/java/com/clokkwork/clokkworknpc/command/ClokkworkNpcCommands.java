package com.clokkwork.clokkworknpc.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;

public final class ClokkworkNpcCommands {

	private ClokkworkNpcCommands() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("clokkworknpc")
						.requires(source -> source.hasPermission(0))
						.then(Commands.literal("spawn")
								.then(Commands.argument("npc_id", ResourceLocationArgument.id())
										.executes(NpcCommandHandlers::spawn)))
		);
	}
}
