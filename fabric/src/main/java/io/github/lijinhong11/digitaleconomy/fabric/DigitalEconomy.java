package io.github.lijinhong11.digitaleconomy.fabric;

import io.github.lijinhong11.digitaleconomy.DigitalEconomyCommon;
import io.github.lijinhong11.digitaleconomy.command.EconomyCommand;
import io.github.lijinhong11.digitaleconomy.command.PayCommand;
import io.github.lijinhong11.digitaleconomy.enums.Side;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class DigitalEconomy implements ModInitializer, ClientModInitializer {
    private static final Path DATA_PATH = FabricLoader.getInstance().getConfigDir().resolve("digitaleconomy");

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> DigitalEconomyCommon.init(DATA_PATH, Side.SERVER, server));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(EconomyCommand.getForRegistration());
            dispatcher.register(PayCommand.getForRegistration());
        });
    }

    @Override
    public void onInitializeClient() {
        DigitalEconomyCommon.init(DATA_PATH, Side.CLIENT, null);
    }
}
