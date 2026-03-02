package com.mycompany.pizza_ordering_app;

import java.math.BigDecimal;
import java.util.List;

public record PriceBreakdown (
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal total,
        List<String> receiptLines
) {}
