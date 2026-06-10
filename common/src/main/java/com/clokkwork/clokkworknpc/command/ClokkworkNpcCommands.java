package com.clokkwork.clokkworknpc.command;

import com.clokkwork.clokkworknpc.reputation.ReputationBounds;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
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
						.then(Commands.literal("reputation")
								.then(Commands.literal("get")
										.then(Commands.argument("player", EntityArgument.player())
												.then(Commands.argument("faction", ResourceLocationArgument.id())
														.executes(ReputationCommandHandlers::get))))
								.then(Commands.literal("set")
										.then(Commands.argument("player", EntityArgument.player())
												.then(Commands.argument("faction", ResourceLocationArgument.id())
														.then(Commands.argument("value", IntegerArgumentType.integer(ReputationBounds.MIN, ReputationBounds.MAX))
																.executes(ReputationCommandHandlers::set)))))
								.then(Commands.literal("add")
										.then(Commands.argument("player", EntityArgument.player())
												.then(Commands.argument("faction", ResourceLocationArgument.id())
														.then(Commands.argument("amount", IntegerArgumentType.integer())
																.executes(ReputationCommandHandlers::add)))))
								.then(Commands.literal("reset")
										.then(Commands.argument("player", EntityArgument.player())
												.then(Commands.argument("faction", ResourceLocationArgument.id())
														.executes(ReputationCommandHandlers::reset))))
		));
	}
}
