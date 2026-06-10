package com.paynova.app.service;

import com.paynova.app.dto.response.QRPaymentResponse;

public interface QRPaymentService {
    QRPaymentResponse getMyQRCode();
    QRPaymentResponse regenerateQRCode();
    QRPaymentResponse lookupQRCode(String qrCode);
}
