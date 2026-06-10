package com.paynova.app.service.impl;

import com.paynova.app.dto.request.QRPayRequest;
import com.paynova.app.dto.request.RefundRequest;
import com.paynova.app.dto.response.PagedResponse;
import com.paynova.app.dto.response.TransactionResponse;
import com.paynova.app.entity.QRPayment;
import com.paynova.app.entity.Transaction;
import com.paynova.app.entity.User;
import com.paynova.app.entity.Wallet;
import com.paynova.app.entity.enums.TransactionStatus;
import com.paynova.app.entity.enums.TransactionType;
import com.paynova.app.entity.enums.UserStatus;
import com.paynova.app.exception.InsufficientBalanceException;
import com.paynova.app.exception.InvalidTransactionException;
import com.paynova.app.exception.ResourceNotFoundException;
import com.paynova.app.exception.UserBlockedException;
import com.paynova.app.repository.QRPaymentRepository;
import com.paynova.app.repository.TransactionRepository;
import com.paynova.app.repository.WalletRepository;
import com.paynova.app.service.TransactionService;
import com.paynova.app.util.EntityMapper;
import com.paynova.app.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final QRPaymentRepository qrPaymentRepository;
    private final SecurityUtils securityUtils;
    private final EntityMapper mapper;

    // ─── User: my transactions ─────────────────────────────────────────────────

    @Override
    public PagedResponse<TransactionResponse> getMyTransactions(Pageable pageable) {
        Long userId = securityUtils.getCurrentUserId();
        Page<Transaction> page = transactionRepository.findAllByUserId(userId, pageable);
        return PagedResponse.from(page.map(mapper::toTransactionResponse));
    }

    @Override
    public PagedResponse<TransactionResponse> getMyTransactionsByType(TransactionType type, Pageable pageable) {
        Long userId = securityUtils.getCurrentUserId();
        Page<Transaction> page = transactionRepository.findByUserIdAndType(userId, type, pageable);
        return PagedResponse.from(page.map(mapper::toTransactionResponse));
    }

    @Override
    public TransactionResponse getTransactionById(String transactionId) {
        User currentUser = securityUtils.getCurrentUser();
        Transaction tx = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "transactionId", transactionId));

        // Non-admins can only view their own transactions
        boolean isSender = tx.getSender() != null && tx.getSender().getId().equals(currentUser.getId());
        boolean isReceiver = tx.getReceiver() != null && tx.getReceiver().getId().equals(currentUser.getId());
        if (!securityUtils.isCurrentUserAdmin() && !isSender && !isReceiver) {
            throw new ResourceNotFoundException("Transaction", "transactionId", transactionId);
        }
        return mapper.toTransactionResponse(tx);
    }

    // ─── QR Pay ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public TransactionResponse payByQR(QRPayRequest request) {
        User sender = securityUtils.getCurrentUser();
        if (sender.getStatus() == UserStatus.BLOCKED) {
            throw new UserBlockedException("Your account is blocked");
        }

        QRPayment qr = qrPaymentRepository.findByQrCode(request.getQrCode())
                .orElseThrow(() -> new ResourceNotFoundException("QR code not found: " + request.getQrCode()));

        if (!qr.getIsActive()) {
            throw new InvalidTransactionException("This QR code is deactivated");
        }

        User receiver = qr.getUser();
        if (sender.getId().equals(receiver.getId())) {
            throw new InvalidTransactionException("Cannot pay yourself via QR");
        }
        if (receiver.getStatus() == UserStatus.BLOCKED) {
            throw new UserBlockedException("Receiver account is blocked");
        }

        // Lock wallets in deterministic order
        Long firstId = Math.min(sender.getId(), receiver.getId());
        Long secondId = Math.max(sender.getId(), receiver.getId());
        Wallet first = walletRepository.findByUserIdForUpdate(firstId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + firstId));
        Wallet second = walletRepository.findByUserIdForUpdate(secondId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + secondId));

        Wallet senderWallet = sender.getId().equals(firstId) ? first : second;
        Wallet receiverWallet = sender.getId().equals(firstId) ? second : first;

        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Available: ₹%.2f", senderWallet.getBalance()));
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(request.getAmount()));
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        Transaction tx = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(request.getAmount())
                .type(TransactionType.QR_PAYMENT)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription() != null ? request.getDescription() : "QR Payment")
                .referenceId(qr.getQrCode())
                .completedAt(LocalDateTime.now())
                .build();
        tx = transactionRepository.save(tx);

        log.info("QR payment: ₹{} from user {} to user {}", request.getAmount(), sender.getId(), receiver.getId());
        return mapper.toTransactionResponse(tx);
    }

    // ─── Admin: refund ─────────────────────────────────────────────────────────

    @Override
    @Transactional
    public TransactionResponse refundTransaction(RefundRequest request) {
        Transaction original = transactionRepository.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction", "transactionId", request.getTransactionId()));

        if (original.getStatus() != TransactionStatus.SUCCESS) {
            throw new InvalidTransactionException(
                    "Only successful transactions can be refunded. Current status: " + original.getStatus());
        }
        if (original.getType() == TransactionType.ADD_MONEY) {
            throw new InvalidTransactionException("ADD_MONEY transactions cannot be refunded through this endpoint");
        }

        // Reverse: take from original receiver, give back to original sender
        User refundFrom = original.getReceiver();
        User refundTo   = original.getSender();

        Long firstId = Math.min(refundFrom.getId(), refundTo.getId());
        Long secondId = Math.max(refundFrom.getId(), refundTo.getId());
        Wallet first = walletRepository.findByUserIdForUpdate(firstId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + firstId));
        Wallet second = walletRepository.findByUserIdForUpdate(secondId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + secondId));

        Wallet fromWallet = refundFrom.getId().equals(firstId) ? first : second;
        Wallet toWallet   = refundFrom.getId().equals(firstId) ? second : first;

        if (fromWallet.getBalance().compareTo(original.getAmount()) < 0) {
            throw new InsufficientBalanceException("Receiver has insufficient balance for refund");
        }

        fromWallet.setBalance(fromWallet.getBalance().subtract(original.getAmount()));
        toWallet.setBalance(toWallet.getBalance().add(original.getAmount()));
        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        // Mark original as refunded
        original.setStatus(TransactionStatus.REFUNDED);
        transactionRepository.save(original);

        // Create refund transaction record
        Transaction refundTx = Transaction.builder()
                .sender(refundFrom)
                .receiver(refundTo)
                .amount(original.getAmount())
                .type(TransactionType.REFUND)
                .status(TransactionStatus.SUCCESS)
                .description("Refund for transaction: " + original.getTransactionId() + " — " + request.getReason())
                .referenceId(original.getTransactionId())
                .completedAt(LocalDateTime.now())
                .build();
        refundTx = transactionRepository.save(refundTx);

        log.info("Refund processed for transaction {} — ₹{}", original.getTransactionId(), original.getAmount());
        return mapper.toTransactionResponse(refundTx);
    }

    // ─── Admin: all transactions ───────────────────────────────────────────────

    @Override
    public PagedResponse<TransactionResponse> getAllTransactions(Pageable pageable) {
        Page<Transaction> page = transactionRepository.findAllByOrderByCreatedAtDesc(pageable);
        return PagedResponse.from(page.map(mapper::toTransactionResponse));
    }
}
