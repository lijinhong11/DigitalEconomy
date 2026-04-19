package io.github.lijinhong11.digitaleconomy.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class PayCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> getForRegistration() {
        return Commands.literal("pay")
    }
}
