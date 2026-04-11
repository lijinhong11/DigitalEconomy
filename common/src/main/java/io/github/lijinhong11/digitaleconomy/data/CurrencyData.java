package io.github.lijinhong11.digitaleconomy.data;

import io.github.lijinhong11.mdatabase.serialization.annotations.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

//Uses currency id as its table name
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyData {
    @Column
    private UUID player;

    @Column
    private BigDecimal balance;
}
