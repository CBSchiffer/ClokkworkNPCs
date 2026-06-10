package com.clokkwork.clokkworknpc.command;

import com.clokkwork.clokkworknpc.faction.Factions;
import com.clokkwork.clokkworknpc.reputation.PlayerReputations;
import com.clokkwork.clokkworknpc.reputation.ReputationBounds;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class ReputationCommandHandlers {

	private ReputationCommandHandlers() {
	}

	public static int get(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(context, "player");
		ResourceLocation factionId = ResourceLocationArgument.getId(context, "faction");
		ServerLevel level = context.getSource().getLevel();

		if (Factions.get(level).get(factionId).isEmpty()) {
			context.getSource().sendFailure(Component.literal("Unknown faction: " + factionId));
			return 0;
		}

		int reputation = PlayerReputations.get(level, target, factionId);
		context.getSource().sendSuccess(
				() -> Component.literal("Reputation for ")
						.append(Component.literal(target.getGameProfile().getName()).withStyle(ChatFormatting.YELLOW))
						.append(Component.literal(" with "))
						.append(Component.literal(factionId.toString()).withStyle(ChatFormatting.AQUA))
						.append(Component.literal(": " + reputation).withStyle(ChatFormatting.GREEN)),
				false
		);
		return reputation;
	}

	public static int set(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(context, "player");
		ResourceLocation factionId = ResourceLocationArgument.getId(context, "faction");
		int value = IntegerArgumentType.getInteger(context, "value");
		ServerLevel level = context.getSource().getLevel();

		if (Factions.get(level).get(factionId).isEmpty()) {
			context.getSource().sendFailure(Component.literal("Unknown faction: " + factionId));
			return 0;
		}

		int result = PlayerReputations.set(level, target.getUUID(), factionId, value);
		context.getSource().sendSuccess(
				() -> Component.literal("Set reputation for ")
						.append(Component.literal(target.getGameProfile().getName()).withStyle(ChatFormatting.YELLOW))
						.append(Component.literal(" with "))
						.append(Component.literal(factionId.toString()).withStyle(ChatFormatting.AQUA))
						.append(Component.literal(" to " + result).withStyle(ChatFormatting.GREEN))
						.append(Component.literal(" (range " + ReputationBounds.MIN + " to " + ReputationBounds.MAX + ")")
								.withStyle(ChatFormatting.GRAY)),
				true
		);
		return result;
	}

	public static int add(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(context, "player");
		ResourceLocation factionId = ResourceLocationArgument.getId(context, "faction");
		int amount = IntegerArgumentType.getInteger(context, "amount");
		ServerLevel level = context.getSource().getLevel();

		if (Factions.get(level).get(factionId).isEmpty()) {
			context.getSource().sendFailure(Component.literal("Unknown faction: " + factionId));
			return 0;
		}

		int result = PlayerReputations.add(level, target.getUUID(), factionId, amount);
		context.getSource().sendSuccess(
				() -> Component.literal("Reputation for ")
						.append(Component.literal(target.getGameProfile().getName()).withStyle(ChatFormatting.YELLOW))
						.append(Component.literal(" with "))
						.append(Component.literal(factionId.toString()).withStyle(ChatFormatting.AQUA))
						.append(Component.literal(" is now " + result).withStyle(ChatFormatting.GREEN)),
				true
		);
		return result;
	}

	public static int reset(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(context, "player");
		ResourceLocation factionId = ResourceLocationArgument.getId(context, "faction");
		ServerLevel level = context.getSource().getLevel();

		if (Factions.get(level).get(factionId).isEmpty()) {
			context.getSource().sendFailure(Component.literal("Unknown faction: " + factionId));
			return 0;
		}

		PlayerReputations.reset(level, target.getUUID(), factionId);
		context.getSource().sendSuccess(
				() -> Component.literal("Reset reputation for ")
						.append(Component.literal(target.getGameProfile().getName()).withStyle(ChatFormatting.YELLOW))
						.append(Component.literal(" with "))
						.append(Component.literal(factionId.toString()).withStyle(ChatFormatting.AQUA))
						.append(Component.literal(" to " + ReputationBounds.DEFAULT).withStyle(ChatFormatting.GREEN)),
				true
		);
		return 1;
	}
}
