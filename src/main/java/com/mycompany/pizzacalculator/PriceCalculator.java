package com.mycompany.pizzacalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PriceCalculator {

    public static PriceBreakdown calculate(
            PizzaSize size,
            List<Topping> toppings,
            int quantity,
            BigDecimal taxRate,
            String couponCode
    ) {
        BigDecimal base = size.getBasePrice();

        BigDecimal toppingTotal = toppings.stream()
                .map(Topping::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subtotal = base.add(toppingTotal)
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal discount = applyCoupon(subtotal, couponCode);
        BigDecimal afterDiscount = subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);

        BigDecimal tax = afterDiscount.multiply(taxRate)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = afterDiscount.add(tax)
                .setScale(2, RoundingMode.HALF_UP);

        List<String> lines = new ArrayList<>();
        lines.add("Size: " + size);
        lines.add("Toppings: " + toppings);
        lines.add("Quantity: " + quantity);
        lines.add("Subtotal: $" + subtotal);
        lines.add("Discount: -$" + discount);
        lines.add("Tax: $" + tax);
        lines.add("Total: $" + total);

        return new PriceBreakdown(subtotal, discount, tax, total, lines);
    }

    private static BigDecimal applyCoupon(BigDecimal subtotal, String code) {
        if (code == null || code.isBlank()) return BigDecimal.ZERO;

        return (switch (code.toUpperCase()) {
            case "SAVE10" -> subtotal.multiply(new BigDecimal("0.10"));
            case "SAVE15" -> subtotal.multiply(new BigDecimal("0.15"));
            case "TAKE5"  -> new BigDecimal("5.00");
            case "BOGO50" -> subtotal.multiply(new BigDecimal("0.25"));
            default -> BigDecimal.ZERO;
        }).setScale(2, RoundingMode.HALF_UP);
    }
}

