package com.paynova.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRPaymentResponse {
    private Long id;
    private String qrCode;
    private String qrImageUrl;   // URL for rendering the QR as an image (frontend handles this)
    private Boolean isActive;
    private LocalDateTime createdAt;
}
