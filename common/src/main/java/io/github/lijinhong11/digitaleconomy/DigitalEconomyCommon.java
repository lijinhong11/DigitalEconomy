package io.github.lijinhong11.digitaleconomy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static DigitalEconomyConfig init(Path configPath) {
        if (!Files.exists(configPath)) {
            try {
                Files.createFile(configPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
