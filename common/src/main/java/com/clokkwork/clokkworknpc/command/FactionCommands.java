package com.clokkwork.clokkworknpc.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class FactionCommands {

	private FactionCommands() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("faction")
						.requires(source -> source.hasPermission(0))
						.then(Commands.literal("list")
								.executes(FactionCommandHandlers::list))
						.then(Commands.literal("create")
								.then(Commands.argument("name", StringArgumentType.string())
										.executes(context -> FactionCommandHandlers.create(context, null))
										.then(Commands.argument("description", StringArgumentType.greedyString())
												.executes(context -> FactionCommandHandlers.create(
														context,
														StringArgumentType.getString(context, "description")
												)))))
		);
	}
}
