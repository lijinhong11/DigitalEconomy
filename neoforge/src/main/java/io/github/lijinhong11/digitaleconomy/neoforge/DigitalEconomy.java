package io.github.lijinhong11.digitaleconomy.neoforge;

import io.github.lijinhong11.digitaleconomy.DigitalEconomyCommon;
import io.github.lijinhong11.digitaleconomy.enums.Side;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.nio.file.Path;

@Mod(DigitalEconomy.MOD_ID)
@Mod.EventBusSubscriber(modid = DigitalEconomy.MOD_ID)
public class DigitalEconomy {
    public static final String MOD_ID = "digitaleconomy";

    private final Path dataPath;

    public DigitalEconomy() {
       dataPath = FMLPaths.CONFIGDIR.get().resolve("treasury");
    }

    @SubscribeEvent
    public void onClientStart(FMLClientSetupEvent e) {
        DigitalEconomyCommon.init(dataPath, Side.CLIENT, null);
    }

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent e) {
        MinecraftServer server = e.getServer();

        DigitalEconomyCommon.init(dataPath, Side.SERVER, server);
    }
}
