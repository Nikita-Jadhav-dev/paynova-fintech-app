package com.paynova.app.repository;

import com.paynova.app.entity.QRPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRPaymentRepository extends JpaRepository<QRPayment, Long> {

    Optional<QRPayment> findByQrCode(String qrCode);

    Optional<QRPayment> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
