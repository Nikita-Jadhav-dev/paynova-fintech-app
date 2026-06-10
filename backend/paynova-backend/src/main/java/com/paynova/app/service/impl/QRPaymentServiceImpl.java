package com.paynova.app.service.impl;

import com.paynova.app.dto.response.QRPaymentResponse;
import com.paynova.app.entity.QRPayment;
import com.paynova.app.entity.User;
import com.paynova.app.exception.ResourceNotFoundException;
import com.paynova.app.repository.QRPaymentRepository;
import com.paynova.app.service.QRPaymentService;
import com.paynova.app.util.EntityMapper;
import com.paynova.app.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QRPaymentServiceImpl implements QRPaymentService {

    private final QRPaymentRepository qrPaymentRepository;
    private final SecurityUtils securityUtils;
    private final EntityMapper mapper;

    @Override
    public QRPaymentResponse getMyQRCode() {
        Long userId = securityUtils.getCurrentUserId();
        QRPayment qr = qrPaymentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("QR code not found for your account"));
        return mapper.toQRPaymentResponse(qr);
    }

    @Override
    @Transactional
    public QRPaymentResponse regenerateQRCode() {
        User user = securityUtils.getCurrentUser();
        QRPayment qr = qrPaymentRepository.findByUserId(user.getId())
                .orElseGet(() -> QRPayment.builder().user(user).isActive(true).build());

        // Deactivate old code and persist new one (PrePersist will generate a new UUID-based code)
        qrPaymentRepository.delete(qr);
        qrPaymentRepository.flush();

        QRPayment newQr = QRPayment.builder()
                .user(user)
                .isActive(true)
                .build();
        newQr = qrPaymentRepository.save(newQr);
        log.info("QR code regenerated for user {}", user.getId());
        return mapper.toQRPaymentResponse(newQr);
    }

    @Override
    public QRPaymentResponse lookupQRCode(String qrCode) {
        QRPayment qr = qrPaymentRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("QR code not found: " + qrCode));
        return mapper.toQRPaymentResponse(qr);
    }
}
