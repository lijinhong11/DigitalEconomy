package io.github.lijinhong11.digitaleconomy.config;

import io.github.lijinhong11.mdatabase.enums.DatabaseType;
import io.github.lijinhong11.treasury.economy.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DigitalEconomyConfig {
    private CurrencySettings defaultCurrency = new CurrencySettings();

    private boolean allowNegativeBalance = false;

    private BigDecimal minimumPayAmount = new BigDecimal("0.01");
    private boolean payToggleDefault = true;

    private DatabaseSettings database = new DatabaseSettings();

    private String currencyDisplayFormat = "#,##0.00";

    private boolean economyLogs = false;
    private String economyLogDateFormat = "dd/MM/yyyy HH:mm:ss";
    private String economyLogAddFormat = "%player% got %value% from %source%";
    private String economyLogTakeFormat = "%player% was taken %value% from %source%";
    private String economyLogPayFormat = "%player% paid %value% to %destination%";

    private List<CurrencySettings> currencies = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseSettings {
        private @NotNull DatabaseType type = DatabaseType.SQLITE;
        private String host = "localhost";
        private int port = 3306;
        private String userName = "root";
        private String password = "123456";
        private String database = "digitaleconomy";
        private String sqliteFile = "digitaleconomy.db";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrencySettings {
        private @NotNull String id = "default";
        private String currencyNameSingular = "coin";
        private String currencyNamePlural = "coins";
        private BigDecimal minBalance = BigDecimal.ZERO;
        private BigDecimal startingBalance = BigDecimal.ZERO;
        private BigDecimal maxBalance = new BigDecimal(Long.MAX_VALUE);

        public final Currency toTreasuryCurrency() {
            return new Currency(id, currencyNameSingular, currencyNamePlural, minBalance, startingBalance, maxBalance);
        }
    }
}
