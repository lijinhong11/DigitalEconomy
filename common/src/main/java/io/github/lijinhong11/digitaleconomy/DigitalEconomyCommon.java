package io.github.lijinhong11.digitaleconomy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import lombok.experimental.UtilityClass;

import java.io.IOException;
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

    private static final Logger LOGGER = Logger.getLogger("DigitalEconomy");

    public static Logger logger() {
        return LOGGER;
    }

    public static DigitalEconomyConfig init(Path dataPath) {
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
}
