package io.github.lijinhong11.digitaleconomy.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(DigitalEconomy.MOD_ID)
public class DigitalEconomy {
    public static final String MOD_ID = "example";

    public DigitalEconomy() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onStartup(FMLCommonSetupEvent e) {

    }
}
