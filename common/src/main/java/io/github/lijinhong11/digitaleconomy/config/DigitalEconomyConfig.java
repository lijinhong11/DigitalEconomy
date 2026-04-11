package io.github.lijinhong11.digitaleconomy.config;

import io.github.lijinhong11.treasury.economy.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DigitalEconomyConfig {
    private BigDecimal maxBalance = new BigDecimal(Double.MAX_VALUE);
    private BigDecimal minBalance = new BigDecimal(0);
    private boolean allowNegativeBalance = false;

    private BigDecimal minimumPayAmount = new BigDecimal("0.01");
    private boolean payToggleDefault = true;

    private DefaultCurrencySettings defaultCurrency = new DefaultCurrencySettings();

    private String currencyDisplayFormat = "#,##0.00";

    private boolean economyLogs = false;
    private String economyLogDateFormat = "dd/MM/yyyy HH:mm:ss";
    private String economyLogAddFormat = "%player% got %value% from %source%";
    private String economyLogTakeFormat = "%player% was taken %value% from %source%";
    private String economyLogPayFormat = "%player% paid %value% to %destination%";

    private List<Currency> currencies = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultCurrencySettings {
        private BigDecimal startingBalance = new BigDecimal(0);
        private String currencyNameSingular = "";
        private String currencyNamePlural = "";
    }
}
