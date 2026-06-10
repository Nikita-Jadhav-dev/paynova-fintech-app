package com.paynova.app.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddMoneyRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum amount is ₹1")
    @DecimalMax(value = "100000.00", message = "Maximum amount is ₹1,00,000")
    private BigDecimal amount;

    private Long bankAccountId;  // optional — if null, use primary account

    private String description;
}
