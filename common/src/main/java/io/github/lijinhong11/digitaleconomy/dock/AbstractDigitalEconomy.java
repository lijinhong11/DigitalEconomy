package io.github.lijinhong11.digitaleconomy.dock;

import io.github.lijinhong11.digitaleconomy.DigitalEconomyCommon;
import io.github.lijinhong11.digitaleconomy.config.DigitalEconomyConfig;
import io.github.lijinhong11.digitaleconomy.data.PlayerDataController;
import io.github.lijinhong11.digitaleconomy.enums.EconomyLogType;
import io.github.lijinhong11.treasury.economy.Currency;
import io.github.lijinhong11.treasury.economy.EconomyProvider;
import io.github.lijinhong11.treasury.economy.EconomyResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AbstractDigitalEconomy implements EconomyProvider {
    private final Currency defaultCurrency;
    private final DigitalEconomyConfig config;
    private final PlayerDataController dataController;
    private final List<Currency> currencies;

    public AbstractDigitalEconomy(DigitalEconomyConfig config, PlayerDataController dataController) {
        this.config = config;
        this.dataController = dataController;
        this.defaultCurrency = config.getDefaultCurrency().toTreasuryCurrency();
        this.currencies = new ArrayList<>();
        this.currencies.add(defaultCurrency);
        config.getCurrencies().forEach(settings -> this.currencies.add(settings.toTreasuryCurrency()));
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
    public Currency defaultCurrency() {
        return defaultCurrency;
    }

    @Override
    public Collection<Currency> currencies() {
        return currencies;
    }

    @Override
    public String format(BigDecimal amount) {
        return new DecimalFormat(config.getCurrencyDisplayFormat()).format(amount);
    }

    @Override
    public String format(Currency currency, BigDecimal amount) {
        return format(amount) + " " + currency.name(amount.doubleValue());
    }

    @Override
    public String currencyNamePlural() {
        return defaultCurrency.pluralName();
    }

    @Override
    public String currencyNameSingular() {
        return defaultCurrency.singularName();
    }

    @Override
    public boolean hasAccount(UUID player) {
        return dataController.hasAccount(player, defaultCurrency);
    }

    @Override
    public boolean hasAccount(String player) {
        return dataController.hasAccount(player, defaultCurrency);
    }

    @Override
    public BigDecimal getBalance(UUID player) {
        return getBalance(player, defaultCurrency);
    }

    @Override
    public BigDecimal getBalance(UUID player, Currency currency) {
        return dataController.getBalance(player, currency);
    }

    @Override
    public BigDecimal getBalance(String player) {
        return getBalance(player, defaultCurrency);
    }

    @Override
    public BigDecimal getBalance(String player, Currency currency) {
        return dataController.getBalance(player, currency);
    }

    @Override
    public void setBalance(UUID player, BigDecimal amount) {
        setBalance(player, defaultCurrency, amount);
    }

    @Override
    public void setBalance(UUID player, Currency currency, BigDecimal amount) {
        dataController.setBalance(player, currency, clamp(currency, amount));
    }

    @Override
    public void setBalance(String player, BigDecimal amount) {
        setBalance(player, defaultCurrency, amount);
    }

    @Override
    public void setBalance(String player, Currency currency, BigDecimal amount) {
        dataController.setBalance(player, currency, clamp(currency, amount));
    }

    @Override
    public EconomyResponse deposit(UUID player, BigDecimal amount) {
        return deposit(player, defaultCurrency, amount);
    }

    @Override
    public EconomyResponse deposit(UUID player, Currency currency, BigDecimal amount) {
        if (!isAmountValid(amount)) {
            return failed(amount, getBalance(player, currency), "Amount must be positive");
        }

        BigDecimal updated = clamp(currency, getBalance(player, currency).add(amount));
        setBalance(player, currency, updated);
        return new EconomyResponse(amount, updated, EconomyResponse.Result.SUCCESS);
    }

    @Override
    public EconomyResponse deposit(String player, BigDecimal amount) {
        return deposit(player, defaultCurrency, amount);
    }

    @Override
    public EconomyResponse deposit(String player, Currency currency, BigDecimal amount) {
        if (!isAmountValid(amount)) {
            return failed(amount, getBalance(player, currency), "Amount must be positive");
        }

        BigDecimal updated = clamp(currency, getBalance(player, currency).add(amount));
        setBalance(player, currency, updated);
        return new EconomyResponse(amount, updated, EconomyResponse.Result.SUCCESS);
    }

    @Override
    public EconomyResponse withdraw(UUID player, BigDecimal amount) {
        return withdraw(player, defaultCurrency, amount);
    }

    @Override
    public EconomyResponse withdraw(UUID player, Currency currency, BigDecimal amount) {
        if (!isAmountValid(amount)) {
            return failed(amount, getBalance(player, currency), "Amount must be positive");
        }

        BigDecimal balance = getBalance(player, currency);
        BigDecimal updated = balance.subtract(amount);
        if (!canSetBalance(currency, updated)) {
            return failed(amount, balance, "Insufficient funds");
        }

        setBalance(player, currency, updated);
        return new EconomyResponse(amount, updated, EconomyResponse.Result.SUCCESS);
    }

    @Override
    public EconomyResponse withdraw(String player, BigDecimal amount) {
        return withdraw(player, defaultCurrency, amount);
    }

    @Override
    public EconomyResponse withdraw(String player, Currency currency, BigDecimal amount) {
        if (!isAmountValid(amount)) {
            return failed(amount, getBalance(player, currency), "Amount must be positive");
        }

        BigDecimal balance = getBalance(player, currency);
        BigDecimal updated = balance.subtract(amount);
        if (!canSetBalance(currency, updated)) {
            return failed(amount, balance, "Insufficient funds");
        }

        setBalance(player, currency, updated);
        return new EconomyResponse(amount, updated, EconomyResponse.Result.SUCCESS);
    }

    @Override
    public boolean has(UUID player, BigDecimal amount) {
        return has(player, defaultCurrency, amount);
    }

    @Override
    public boolean has(UUID player, Currency currency, BigDecimal amount) {
        return getBalance(player, currency).compareTo(amount) >= 0;
    }

    @Override
    public boolean has(String player, BigDecimal amount) {
        return has(player, defaultCurrency, amount);
    }

    @Override
    public boolean has(String player, Currency currency, BigDecimal amount) {
        return getBalance(player, currency).compareTo(amount) >= 0;
    }

    @Override
    public EconomyResponse transfer(UUID from, UUID to, BigDecimal amount) {
        return transfer(from, to, defaultCurrency, amount);
    }

    @Override
    public EconomyResponse transfer(UUID from, UUID to, Currency currency, BigDecimal amount) {
        EconomyResponse withdrawn = withdraw(from, currency, amount);
        if (!withdrawn.operationSuccess()) {
            return withdrawn;
        }

        EconomyResponse deposited = deposit(to, currency, amount);
        if (!deposited.operationSuccess()) {
            deposit(from, currency, amount);
            return deposited;
        }

        return deposited;
    }

    @Override
    public EconomyResponse transfer(String from, String to, BigDecimal amount) {
        return transfer(from, to, defaultCurrency, amount);
    }

    @Override
    public EconomyResponse transfer(String from, String to, Currency currency, BigDecimal amount) {
        EconomyResponse withdrawn = withdraw(from, currency, amount);
        if (!withdrawn.operationSuccess()) {
            return withdrawn;
        }

        EconomyResponse deposited = deposit(to, currency, amount);
        if (!deposited.operationSuccess()) {
            deposit(from, currency, amount);
            return deposited;
        }

        return deposited;
    }

    public EconomyResponse add(UUID player, Currency currency, BigDecimal amount) {
        EconomyResponse response = deposit(player, currency, amount);
        if (response.operationSuccess()) {
            DigitalEconomyCommon.economyLog(EconomyLogType.ADD, player, null, amount);
        }
        return response;
    }

    public EconomyResponse take(UUID player, Currency currency, BigDecimal amount) {
        EconomyResponse response = withdraw(player, currency, amount);
        if (response.operationSuccess()) {
            DigitalEconomyCommon.economyLog(EconomyLogType.TAKE, player, null, amount);
        }
        return response;
    }

    public EconomyResponse pay(UUID from, UUID to, BigDecimal amount) {
        EconomyResponse response = transfer(from, to, defaultCurrency, amount);
        if (response.operationSuccess()) {
            DigitalEconomyCommon.economyLog(EconomyLogType.PAY, from, to, amount);
        }
        return response;
    }

    private EconomyResponse failed(BigDecimal amount, BigDecimal balance, String message) {
        return new EconomyResponse(amount, balance, EconomyResponse.Result.FAILED, message);
    }

    private boolean isAmountValid(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean canSetBalance(Currency currency, BigDecimal balance) {
        DigitalEconomyConfig.CurrencySettings settings = dataController.getCurrencySettings(currency);
        BigDecimal minBalance = settings.getMinBalance().max(BigDecimal.ZERO);
        return balance.compareTo(minBalance) >= 0 && balance.compareTo(settings.getMaxBalance()) <= 0;
    }

    private BigDecimal clamp(Currency currency, BigDecimal balance) {
        DigitalEconomyConfig.CurrencySettings settings = dataController.getCurrencySettings(currency);
        BigDecimal minBalance = config.isAllowNegativeBalance()
                ? settings.getMinBalance()
                : settings.getMinBalance().max(BigDecimal.ZERO);
        return balance.max(minBalance).min(settings.getMaxBalance());
    }
}
