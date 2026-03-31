package com.example.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ExampleMod.MOD_ID)
public class ExampleMod {
    public static final String MOD_ID = "example";

    public ExampleMod() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onStartup(FMLCommonSetupEvent e) {

    }
}
