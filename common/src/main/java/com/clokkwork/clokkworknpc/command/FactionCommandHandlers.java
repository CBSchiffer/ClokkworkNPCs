package com.clokkwork.clokkworknpc.command;

import com.clokkwork.clokkworknpc.data.faction.FactionDefinition;
import com.clokkwork.clokkworknpc.faction.FactionIds;
import com.clokkwork.clokkworknpc.faction.Factions;
import com.clokkwork.clokkworknpc.registry.FactionRegistry;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public final class FactionCommandHandlers {

	private FactionCommandHandlers() {
	}

	public static int list(CommandContext<CommandSourceStack> context) {
		CommandSourceStack source = context.getSource();
		ServerLevel level = source.getLevel();
		FactionRegistry registry = Factions.get(level);

		if (registry.isEmpty()) {
			broadcastToWorld(source.getServer(), Component.literal("No factions registered.").withStyle(ChatFormatting.GRAY));
			return 0;
		}

		broadcastToWorld(source.getServer(), Component.literal("Factions:").withStyle(ChatFormatting.GOLD));
		for (FactionDefinition definition : registry.getAll()) {
			broadcastToWorld(source.getServer(), formatFactionLine(definition));
		}
		return registry.getAll().size();
	}

	public static int create(CommandContext<CommandSourceStack> context, String description) throws CommandSyntaxException {
		CommandSourceStack source = context.getSource();
		String name = context.getArgument("name", String.class);
		ServerLevel level = source.getLevel();
		FactionRegistry registry = Factions.get(level);

		ResourceLocation id = FactionIds.worldFactionIdFromTitle(name, registry);
		Optional<String> optionalDescription = description == null || description.isBlank()
				? Optional.empty()
				: Optional.of(description.trim());

		FactionDefinition definition = new FactionDefinition(id, name.trim(), optionalDescription);
		if (!registry.register(definition)) {
			source.sendFailure(Component.literal("Could not create faction; ID already exists: " + id));
			return 0;
		}

		broadcastToWorld(
				source.getServer(),
				Component.literal("Created faction ")
						.withStyle(ChatFormatting.GREEN)
						.append(Component.literal("[" + id + "] " + name).withStyle(ChatFormatting.YELLOW))
		);
		return 1;
	}

	private static Component formatFactionLine(FactionDefinition definition) {
		var line = Component.literal("  [" + definition.id() + "] " + definition.title()).withStyle(ChatFormatting.AQUA);
		if (definition.description().filter(description -> !description.isBlank()).isPresent()) {
			line = line.copy().append(
					Component.literal(" — " + definition.description().get()).withStyle(ChatFormatting.GRAY)
			);
		}
		return line;
	}

	private static void broadcastToWorld(MinecraftServer server, Component message) {
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			player.sendSystemMessage(message);
		}
	}
}
