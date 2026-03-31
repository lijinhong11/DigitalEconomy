package io.github.lijinhong11.digitaleconomy.dock;

import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import io.github.lijinhong11.treasury.economy.Currency;
import io.github.lijinhong11.treasury.economy.EconomyProvider;
import io.github.lijinhong11.treasury.economy.EconomyResponse;

import java.text.DecimalFormat;
import java.util.UUID;

public abstract class AbstractDigitalEconomy implements EconomyProvider {
    private final DigitalEconomyConfig config;

    public AbstractDigitalEconomy(DigitalEconomyConfig config) {
        this.config = config;
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
    public String format(double amount) {
        DecimalFormat df = new DecimalFormat(config.getCurrencyDisplayFormat());
        return df.format(amount);
    }

    @Override
    public String format(Currency currency, double amount) {
        return format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return config.getCurrencyNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return config.getCurrencyNameSingular();
    }

    @Override
    public double getBalance(UUID player, Currency currency) {
        return getBalance(player);
    }

    @Override
    public double getBalance(String player, Currency currency) {
        return getBalance(player);
    }

    @Override
    public void setBalance(UUID player, Currency currency, double amount) {
        setBalance(player, amount);
    }

    @Override
    public void setBalance(String player, Currency currency, double amount) {
        setBalance(player, amount);
    }

    @Override
    public EconomyResponse deposit(UUID player, Currency currency, double amount) {
        return deposit(player, amount);
    }

    @Override
    public EconomyResponse deposit(String player, Currency currency, double amount) {
        return deposit(player, amount);
    }

    @Override
    public EconomyResponse withdraw(UUID player, Currency currency, double amount) {
        return withdraw(player, amount);
    }

    @Override
    public EconomyResponse withdraw(String player, Currency currency, double amount) {
        return withdraw(player, amount);
    }

    @Override
    public boolean has(UUID player, Currency currency, double amount) {
        return has(player, amount);
    }

    @Override
    public boolean has(String player, Currency currency, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse transfer(UUID from, UUID to, Currency currency, double amount) {
        return transfer(from, to, amount);
    }

    @Override
    public EconomyResponse transfer(String from, String to, Currency currency, double amount) {
        return transfer(from, to, amount);
    }
}
