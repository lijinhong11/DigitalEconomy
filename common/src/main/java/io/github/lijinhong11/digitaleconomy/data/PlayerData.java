package io.github.lijinhong11.digitaleconomy.data;

import io.github.lijinhong11.mdatabase.serialization.annotations.Column;
import io.github.lijinhong11.mdatabase.serialization.annotations.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "players")
public class PlayerData {
    @Column
    private UUID uuid;

    @Column
    private BigDecimal balance;

}
