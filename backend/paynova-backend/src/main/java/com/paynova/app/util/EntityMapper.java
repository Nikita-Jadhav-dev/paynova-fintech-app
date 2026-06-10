package com.paynova.app.util;

import com.paynova.app.entity.BankAccount;
import com.paynova.app.entity.QRPayment;
import com.paynova.app.entity.Transaction;
import com.paynova.app.entity.User;
import com.paynova.app.entity.Wallet;
import com.paynova.app.dto.response.*;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    // ─── User ─────────────────────────────────────────────────────────────────

    public UserResponse toUserResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .upiId(user.getUpiId())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // ─── Wallet ───────────────────────────────────────────────────────────────

    public WalletResponse toWalletResponse(Wallet wallet) {
        if (wallet == null) return null;
        return WalletResponse.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .isActive(wallet.getIsActive())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }

    // ─── BankAccount ──────────────────────────────────────────────────────────

    public BankAccountResponse toBankAccountResponse(BankAccount account) {
        if (account == null) return null;
        return BankAccountResponse.builder()
                .id(account.getId())
                .accountNumber(maskAccountNumber(account.getAccountNumber()))
                .ifscCode(account.getIfscCode())
                .bankName(account.getBankName())
                .accountHolderName(account.getAccountHolderName())
                .balance(account.getBalance())
                .isPrimary(account.getIsPrimary())
                .isVerified(account.getIsVerified())
                .createdAt(account.getCreatedAt())
                .build();
    }

    // ─── Transaction ──────────────────────────────────────────────────────────

    public TransactionResponse toTransactionResponse(Transaction tx) {
        if (tx == null) return null;
        return TransactionResponse.builder()
                .id(tx.getId())
                .transactionId(tx.getTransactionId())
                .amount(tx.getAmount())
                .type(tx.getType())
                .status(tx.getStatus())
                .description(tx.getDescription())
                .failureReason(tx.getFailureReason())
                .createdAt(tx.getCreatedAt())
                .completedAt(tx.getCompletedAt())
                .senderName(tx.getSender() != null ? tx.getSender().getName() : null)
                .senderPhone(tx.getSender() != null ? tx.getSender().getPhone() : null)
                .receiverName(tx.getReceiver() != null ? tx.getReceiver().getName() : null)
                .receiverPhone(tx.getReceiver() != null ? tx.getReceiver().getPhone() : null)
                .build();
    }

    // ─── QRPayment ────────────────────────────────────────────────────────────

    public QRPaymentResponse toQRPaymentResponse(QRPayment qr) {
        if (qr == null) return null;
        return QRPaymentResponse.builder()
                .id(qr.getId())
                .qrCode(qr.getQrCode())
                .qrImageUrl("/api/qr/image/" + qr.getQrCode())  // frontend renders QR from this
                .isActive(qr.getIsActive())
                .createdAt(qr.getCreatedAt())
                .build();
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) return accountNumber;
        return "x".repeat(accountNumber.length() - 4) + accountNumber.substring(accountNumber.length() - 4);
    }
}
