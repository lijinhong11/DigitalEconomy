package io.github.lijinhong11.digitaleconomy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import io.github.lijinhong11.digitaleconomy.enums.EconomyLogType;
import io.github.lijinhong11.digitaleconomy.enums.Side;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

@UtilityClass
public class DigitalEconomyCommon {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setStrictness(Strictness.LENIENT)
            .create();

    @Getter
    private static Side side;

    private static MinecraftServer server;

    private static final Logger LOGGER = Logger.getLogger("DigitalEconomy");

    public static Logger logger() {
        return LOGGER;
    }

    public static DigitalEconomyConfig init(Path dataPath, Side side, @Nullable MinecraftServer server) {
        DigitalEconomyCommon.side = side;

        if (side == Side.SERVER) {
            DigitalEconomyCommon.server = server;
        }

        try {
            Files.createDirectories(dataPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path configPath = dataPath.resolve("config.json");

        if (!Files.exists(configPath)) {
            try {
                Files.writeString(configPath, GSON.toJson(new DigitalEconomyConfig()), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            DigitalEconomyConfig cfg = GSON.fromJson(Files.readString(configPath), DigitalEconomyConfig.class);

            if (cfg.isEconomyLogs()) {
                Path logs = dataPath.resolve("logs");
                Files.createDirectories(logs);
            }

            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nullable Player getPlayer(String id) {
        if (side == Side.CLIENT) {
            if (Minecraft.getInstance().hasSingleplayerServer()) {
                IntegratedServer cServer = Minecraft.getInstance().getSingleplayerServer();
                if (cServer != null && cServer.isPublished()) {
                    return cServer.getPlayerList().getPlayerByName(id);
                } else {
                    LocalPlayer local = Minecraft.getInstance().player;
                    if (local == null) {
                        return null;
                    }

                    if (local.getName().equals(Component.literal(id))) {
                        return local;
                    }
                }
            }

            return null;
        } else {
            if (server != null) {
                return server.getPlayerList().getPlayerByName(id);
            } else {
                return null;
            }
        }
    }

    public static void economyLog(EconomyLogType type, Player source, @Nullable Player to, BigDecimal value) {

    }
}
