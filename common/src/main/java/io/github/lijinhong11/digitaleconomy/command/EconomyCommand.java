package io.github.lijinhong11.digitaleconomy.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.lijinhong11.digitaleconomy.DigitalEconomyCommon;
import io.github.lijinhong11.digitaleconomy.dock.AbstractDigitalEconomy;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.math.BigDecimal;

public class EconomyCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> getForRegistration() {
        return Commands.literal("economy")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("balance")
                        .executes(ctx -> showBalance(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> showBalance(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))))
                .then(Commands.literal("set")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                                        .executes(ctx -> {
                                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                            BigDecimal amount = BigDecimal.valueOf(DoubleArgumentType.getDouble(ctx, "amount"));
                                            economy().setBalance(player.getUUID(), amount);
                                            ctx.getSource().sendSuccess(() -> Component.literal("Set " + player.getGameProfile().getName() + "'s balance to " + economy().format(amount)), true);
                                            return 1;
                                        }))))
                .then(Commands.literal("add")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0.0000001))
                                        .executes(ctx -> {
                                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                            BigDecimal amount = BigDecimal.valueOf(DoubleArgumentType.getDouble(ctx, "amount"));
                                            var response = economy().add(player.getUUID(), economy().defaultCurrency(), amount);
                                            sendResponse(ctx.getSource(), response.operationSuccess(), "Added " + economy().format(amount) + " to " + player.getGameProfile().getName(), response.errorMessage());
                                            return response.operationSuccess() ? 1 : 0;
                                        }))))
                .then(Commands.literal("take")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0.0000001))
                                        .executes(ctx -> {
                                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                            BigDecimal amount = BigDecimal.valueOf(DoubleArgumentType.getDouble(ctx, "amount"));
                                            var response = economy().take(player.getUUID(), economy().defaultCurrency(), amount);
                                            sendResponse(ctx.getSource(), response.operationSuccess(), "Took " + economy().format(amount) + " from " + player.getGameProfile().getName(), response.errorMessage());
                                            return response.operationSuccess() ? 1 : 0;
                                        }))));
    }

    private static int showBalance(CommandSourceStack source, ServerPlayer player) {
        BigDecimal balance = economy().getBalance(player.getUUID());
        source.sendSuccess(() -> Component.literal(player.getGameProfile().getName() + " has " + economy().format(balance)), false);
        return 1;
    }

    private static void sendResponse(CommandSourceStack source, boolean success, String successMessage, String failureMessage) {
        if (success) {
            source.sendSuccess(() -> Component.literal(successMessage), true);
        } else {
            source.sendFailure(Component.literal(failureMessage == null ? "Economy operation failed" : failureMessage));
        }
    }

    private static AbstractDigitalEconomy economy() {
        return DigitalEconomyCommon.getEconomyProvider();
    }
}
