package com.mycompany.pizzacalculator;

import java.math.BigDecimal;

public enum Topping {

    PEPPERONI("Pepperoni", new BigDecimal("1.50")),
    SAUSAGE("Sausage", new BigDecimal("1.50")),
    MUSHROOMS("Mushrooms", new BigDecimal("1.00")),
    ONIONS("Onions", new BigDecimal("1.00")),
    EXTRA_CHEESE("Extra Cheese", new BigDecimal("1.75")),
    OLIVES("Olives", new BigDecimal("1.00"));

    private final String displayName;
    private final BigDecimal price;

    Topping(String displayName, BigDecimal price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
