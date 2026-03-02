package com.mycompany.pizzacalculator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {

    @Test
    void testCalculationProducesPositiveTotal() {
        PriceBreakdown breakdown = PriceCalculator.calculate(
                PizzaSize.SMALL,
                List.of(Topping.PEPPERONI),
                1,
                new BigDecimal("0.08"),
                "SAVE10"
        );

        assertTrue(breakdown.total().compareTo(BigDecimal.ZERO) > 0);
        assertNotNull(breakdown.receiptLines());
        assertFalse(breakdown.receiptLines().isEmpty());
    }
}
