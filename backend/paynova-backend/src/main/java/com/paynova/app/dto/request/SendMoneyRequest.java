package com.paynova.app.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SendMoneyRequest {

    // Receiver can be identified by phone, email, or UPI ID — at least one required
    private String receiverPhone;
    private String receiverEmail;
    private String receiverUpiId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum transfer amount is ₹1")
    @DecimalMax(value = "100000.00", message = "Maximum transfer amount is ₹1,00,000")
    private BigDecimal amount;

    private String description;
}
