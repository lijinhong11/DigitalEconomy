package io.github.lijinhong11.digitaleconomy.data;

import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import io.github.lijinhong11.mdatabase.DatabaseParameters;
import io.github.lijinhong11.mdatabase.DatabaseConnection;
import io.github.lijinhong11.mdatabase.enums.DatabaseType;
import io.github.lijinhong11.mdatabase.impl.DatabaseConnections;
import io.github.lijinhong11.mdatabase.sql.conditions.Conditions;
import io.github.lijinhong11.mdatabase.sql.sentence.SQL;
import io.github.lijinhong11.treasury.economy.Currency;

import java.nio.file.Path;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

public class PlayerDataController {
    private static final String TABLE = "balances";

    private final DatabaseConnection connection;
    private final DigitalEconomyConfig config;

    public PlayerDataController(DigitalEconomyConfig config, Path dataPath) {
        this.config = config;
        this.connection = createConnection(config.getDatabase(), dataPath);
        createTables();
    }

    public boolean hasAccount(UUID player, Currency currency) {
        return hasAccount(player.toString(), currency);
    }

    public boolean hasAccount(String player, Currency currency) {
        try (ResultSet rs = connection.query(SQL.select()
                .from(TABLE)
                .column("balance")
                .where(Conditions.and(
                        Conditions.eq("player", normalizePlayer(player)),
                        Conditions.eq("currency", currency.id())
                ))
                .limit(1))) {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check account", e);
        }
    }

    public BigDecimal getBalance(UUID player, Currency currency) {
        return getBalance(player.toString(), currency);
    }

    public BigDecimal getBalance(String player, Currency currency) {
        String account = normalizePlayer(player);

        try (ResultSet rs = connection.query(SQL.select()
                .from(TABLE)
                .column("balance")
                .where(Conditions.and(
                        Conditions.eq("player", account),
                        Conditions.eq("currency", currency.id())
                ))
                .limit(1))) {
            if (rs.next()) {
                return rs.getBigDecimal("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read balance", e);
        }

        BigDecimal startingBalance = getCurrencySettings(currency).getStartingBalance();
        setBalance(account, currency, startingBalance);
        return startingBalance;
    }

    public void setBalance(UUID player, Currency currency, BigDecimal balance) {
        setBalance(player.toString(), currency, balance);
    }

    public void setBalance(String player, Currency currency, BigDecimal balance) {
        String account = normalizePlayer(player);

        try {
            if (hasAccount(account, currency)) {
                connection.execute(SQL.update()
                        .table(TABLE)
                        .set("balance", balance)
                        .where(Conditions.and(
                                Conditions.eq("player", account),
                                Conditions.eq("currency", currency.id())
                        )));
            } else {
                connection.execute(SQL.insert()
                        .into(TABLE)
                        .value("player", account)
                        .value("currency", currency.id())
                        .value("balance", balance));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save balance", e);
        }
    }

    public DigitalEconomyConfig.CurrencySettings getCurrencySettings(Currency currency) {
        if (config.getDefaultCurrency().getId().equalsIgnoreCase(currency.id())) {
            return config.getDefaultCurrency();
        }

        return config.getCurrencies().stream()
                .filter(settings -> settings.getId().equalsIgnoreCase(currency.id()))
                .findFirst()
                .orElse(config.getDefaultCurrency());
    }

    private static DatabaseConnection createConnection(DigitalEconomyConfig.DatabaseSettings settings, Path dataPath) {
        DatabaseParameters parameters = new DatabaseParameters();

        if (settings.getType() == DatabaseType.SQLITE) {
            return DatabaseConnections.sqlite(dataPath.resolve(settings.getSqliteFile()).toString(), parameters);
        }

        return DatabaseConnections.createByType(
                settings.getType(),
                dataPath.toFile(),
                settings.getHost(),
                settings.getPort(),
                settings.getDatabase(),
                settings.getUserName(),
                settings.getPassword(),
                parameters
        );
    }

    private void createTables() {
        try {
            connection.execute(SQL.createTable()
                    .table(TABLE)
                    .ifNotExists()
                    .column("player", "VARCHAR", 64)
                    .column("currency", "VARCHAR", 64)
                    .column("balance", "DECIMAL", 32)
                    .notNull("player")
                    .notNull("currency")
                    .notNull("balance")
                    .primaryKey("player", "currency"));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create balance table", e);
        }
    }

    private String normalizePlayer(String player) {
        try {
            return UUID.fromString(player).toString();
        } catch (IllegalArgumentException ignored) {
            return player.toLowerCase(Locale.ROOT);
        }
    }
}
