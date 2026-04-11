package io.github.lijinhong11.digitaleconomy.dock;

import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import io.github.lijinhong11.treasury.economy.Currency;
import io.github.lijinhong11.treasury.economy.EconomyProvider;
import io.github.lijinhong11.treasury.economy.EconomyResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.UUID;

public abstract class AbstractDigitalEconomy implements EconomyProvider {
    private final Currency defaultCurrency;
    private final DigitalEconomyConfig config;

    public AbstractDigitalEconomy(DigitalEconomyConfig config) {
        this.config = config;

        this.defaultCurrency = new Currency("default", config.getDefaultCurrency().getCurrencyNameSingular(), config.getDefaultCurrency().getCurrencyNamePlural());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "DigitalEconomy";
    }

    @Override
    public String format(BigDecimal amount) {
        DecimalFormat df = new DecimalFormat(config.getCurrencyDisplayFormat());
        return df.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return config.getDefaultCurrency().getCurrencyNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return config.getDefaultCurrency().getCurrencyNameSingular();
    }

    @Override
    public BigDecimal getBalance(UUID player) {
        return getBalance(player, defaultCurrency());
    }

    @Override
    public BigDecimal getBalance(String player) {
        return getBalance(player, defaultCurrency());
    }

    @Override
    public void setBalance(UUID player, BigDecimal amount) {
        setBalance(player, defaultCurrency(), amount);
    }

    @Override
    public void setBalance(String player, BigDecimal amount) {
        setBalance(player, defaultCurrency(), amount);
    }

    @Override
    public EconomyResponse deposit(UUID player, BigDecimal amount) {
        return deposit(player, defaultCurrency(), amount);
    }

    @Override
    public EconomyResponse deposit(String player, BigDecimal amount) {
        return deposit(player, defaultCurrency(), amount);
    }

    @Override
    public EconomyResponse withdraw(UUID player, BigDecimal amount) {
        return withdraw(player, defaultCurrency(), amount);
    }

    @Override
    public EconomyResponse withdraw(String player, BigDecimal amount) {
        return withdraw(player, defaultCurrency(), amount);
    }

    @Override
    public boolean has(UUID player, BigDecimal amount) {
        return has(player, defaultCurrency(), amount);
    }

    @Override
    public boolean has(String player, BigDecimal amount) {
        return has(player, defaultCurrency(), amount);
    }

    @Override
    public EconomyResponse transfer(UUID from, UUID to, BigDecimal amount) {
        return transfer(from, to, defaultCurrency(), amount);
    }

    @Override
    public EconomyResponse transfer(String from, String to, BigDecimal amount) {
        return transfer(from, to, defaultCurrency(), amount);
    }

    protected boolean isDefaultCurrency(Currency currency) {
        return defaultCurrency.is(currency.id());
    }
}
