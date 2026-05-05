package io.github.lijinhong11.digitaleconomy.neoforge;

import io.github.lijinhong11.digitaleconomy.DigitalEconomyCommon;
import io.github.lijinhong11.digitaleconomy.command.EconomyCommand;
import io.github.lijinhong11.digitaleconomy.command.PayCommand;
import io.github.lijinhong11.digitaleconomy.enums.Side;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.nio.file.Path;

@Mod(DigitalEconomy.MOD_ID)
public class DigitalEconomy {
    public static final String MOD_ID = "digitaleconomy";

    private final Path dataPath;

    public DigitalEconomy(IEventBus modEventBus) {
       dataPath = FMLPaths.CONFIGDIR.get().resolve("digitaleconomy");
       modEventBus.addListener(this::onClientSetup);
       NeoForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(FMLClientSetupEvent e) {
        DigitalEconomyCommon.init(dataPath, Side.CLIENT, null);
    }

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent e) {
        MinecraftServer server = e.getServer();

        DigitalEconomyCommon.init(dataPath, Side.SERVER, server);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent e) {
        e.getDispatcher().register(EconomyCommand.getForRegistration());
        e.getDispatcher().register(PayCommand.getForRegistration());
    }
}
