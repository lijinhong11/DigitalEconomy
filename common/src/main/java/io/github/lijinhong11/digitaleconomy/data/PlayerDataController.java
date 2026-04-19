package io.github.lijinhong11.digitaleconomy.data;

import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import io.github.lijinhong11.mdatabase.DatabaseConnection;
import io.github.lijinhong11.mdatabase.impl.DatabaseConnections;

public class PlayerDataController {
    private final DatabaseConnection connection;

    public PlayerDataController(DigitalEconomyConfig config) {
        this.connection = DatabaseConnections.createByType(config.getDatabase().getType(), config.getDatabase().)
    }
}
