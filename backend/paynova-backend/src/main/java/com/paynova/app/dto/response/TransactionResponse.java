package com.paynova.app.dto.response;

import com.paynova.app.entity.enums.TransactionStatus;
import com.paynova.app.entity.enums.TransactionType;
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
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    // Sender / receiver summary
    private String senderName;
    private String senderPhone;
    private String receiverName;
    private String receiverPhone;
}
