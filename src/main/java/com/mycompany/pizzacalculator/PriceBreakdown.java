package com.mycompany.pizzacalculator;

import java.math.BigDecimal;
import java.util.List;

public record PriceBreakdown (
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal total,
        List<String> receiptLines
) {}
