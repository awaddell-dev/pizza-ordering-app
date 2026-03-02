package com.mycompany.pizzacalculator;

import java.math.BigDecimal;

public enum PizzaSize {
    SMALL(new BigDecimal("8.00")),
    MEDIUM(new BigDecimal("10.00")),
    LARGE(new BigDecimal("12.00"));

    private final BigDecimal basePrice;

    PizzaSize(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }
}
