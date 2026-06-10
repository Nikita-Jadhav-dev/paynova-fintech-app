package com.paynova.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponse {
    private Long id;
    private String accountNumber;   // masked: xxxxxxxx1234
    private String ifscCode;
    private String bankName;
    private String accountHolderName;
    private BigDecimal balance;
    private Boolean isPrimary;
    private Boolean isVerified;
    private LocalDateTime createdAt;
}
