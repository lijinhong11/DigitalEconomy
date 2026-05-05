package io.github.lijinhong11.digitaleconomy.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.lijinhong11.digitaleconomy.DigitalEconomyCommon;
import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import io.github.lijinhong11.digitaleconomy.dock.AbstractDigitalEconomy;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.math.BigDecimal;

public class PayCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> getForRegistration() {
        return Commands.literal("pay")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0.0000001))
                                .executes(ctx -> {
                                    ServerPlayer source = ctx.getSource().getPlayerOrException();
                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                                    BigDecimal amount = BigDecimal.valueOf(DoubleArgumentType.getDouble(ctx, "amount"));
                                    DigitalEconomyConfig config = DigitalEconomyCommon.getConfig();

                                    if (source.getUUID().equals(target.getUUID())) {
                                        ctx.getSource().sendFailure(Component.literal("You cannot pay yourself"));
                                        return 0;
                                    }

                                    if (config != null && amount.compareTo(config.getMinimumPayAmount()) < 0) {
                                        ctx.getSource().sendFailure(Component.literal("Minimum pay amount is " + economy().format(config.getMinimumPayAmount())));
                                        return 0;
                                    }

                                    var response = economy().pay(source.getUUID(), target.getUUID(), amount);
                                    if (!response.operationSuccess()) {
                                        ctx.getSource().sendFailure(Component.literal(response.errorMessage() == null ? "Payment failed" : response.errorMessage()));
                                        return 0;
                                    }

                                    String formatted = economy().format(amount);
                                    source.sendSystemMessage(Component.literal("Paid " + formatted + " to " + target.getGameProfile().getName()));
                                    target.sendSystemMessage(Component.literal("Received " + formatted + " from " + source.getGameProfile().getName()));
                                    return 1;
                                })));
    }

    private static AbstractDigitalEconomy economy() {
        return DigitalEconomyCommon.getEconomyProvider();
    }
}
