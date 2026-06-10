package com.paynova.app.service.impl;

import com.paynova.app.dto.request.AddMoneyRequest;
import com.paynova.app.dto.request.SendMoneyRequest;
import com.paynova.app.dto.response.TransactionResponse;
import com.paynova.app.dto.response.WalletResponse;
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
import com.paynova.app.repository.TransactionRepository;
import com.paynova.app.repository.UserRepository;
import com.paynova.app.repository.WalletRepository;
import com.paynova.app.service.WalletService;
import com.paynova.app.util.EntityMapper;
import com.paynova.app.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final SecurityUtils securityUtils;
    private final EntityMapper mapper;

    @Value("${app.wallet.max-transfer-limit}")
    private BigDecimal maxTransferLimit;

    @Value("${app.wallet.min-transfer-amount}")
    private BigDecimal minTransferAmount;

    // ─── Get wallet ───────────────────────────────────────────────────────────

    @Override
    public WalletResponse getMyWallet() {
        User user = securityUtils.getCurrentUser();
        Wallet wallet = getWalletOrThrow(user.getId());
        return mapper.toWalletResponse(wallet);
    }

    // ─── Add money ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public TransactionResponse addMoney(AddMoneyRequest request) {
        User user = securityUtils.getCurrentUser();
        validateUserActive(user);
        validateAmount(request.getAmount());

        // Pessimistic lock on wallet row
        Wallet wallet = walletRepository.findByUserIdForUpdate(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + user.getId()));

        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        walletRepository.save(wallet);

        Transaction tx = Transaction.builder()
                .receiver(user)
                .amount(request.getAmount())
                .type(TransactionType.ADD_MONEY)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription() != null ? request.getDescription() : "Added money to wallet")
                .completedAt(LocalDateTime.now())
                .build();
        tx = transactionRepository.save(tx);

        log.info("Money added: ₹{} to wallet of user {}", request.getAmount(), user.getId());
        return mapper.toTransactionResponse(tx);
    }

    // ─── Send money ───────────────────────────────────────────────────────────

    @Override
    @Transactional
    public TransactionResponse sendMoney(SendMoneyRequest request) {
        User sender = securityUtils.getCurrentUser();
        validateUserActive(sender);
        validateAmount(request.getAmount());

        // Resolve receiver
        User receiver = resolveReceiver(request);

        if (sender.getId().equals(receiver.getId())) {
            throw new InvalidTransactionException("Cannot send money to yourself");
        }
        if (receiver.getStatus() == UserStatus.BLOCKED) {
            throw new UserBlockedException("Receiver account is blocked");
        }

        // Lock both wallets in consistent order (lower ID first) to prevent deadlock
        Long firstId = Math.min(sender.getId(), receiver.getId());
        Long secondId = Math.max(sender.getId(), receiver.getId());

        Wallet firstWallet = walletRepository.findByUserIdForUpdate(firstId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + firstId));
        Wallet secondWallet = walletRepository.findByUserIdForUpdate(secondId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + secondId));

        Wallet senderWallet = sender.getId().equals(firstId) ? firstWallet : secondWallet;
        Wallet receiverWallet = sender.getId().equals(firstId) ? secondWallet : firstWallet;

        // Balance check
        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Available: ₹%.2f, Requested: ₹%.2f",
                            senderWallet.getBalance(), request.getAmount()));
        }

        // Transfer
        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(request.getAmount()));
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        Transaction tx = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(request.getAmount())
                .type(TransactionType.SEND)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription() != null ? request.getDescription() : "Money transfer")
                .completedAt(LocalDateTime.now())
                .build();
        tx = transactionRepository.save(tx);

        log.info("Transfer: ₹{} from user {} to user {}", request.getAmount(), sender.getId(), receiver.getId());
        return mapper.toTransactionResponse(tx);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private Wallet getWalletOrThrow(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + userId));
    }

    private void validateUserActive(User user) {
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new UserBlockedException("Your account is blocked. Please contact support.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(minTransferAmount) < 0) {
            throw new InvalidTransactionException("Minimum transaction amount is ₹" + minTransferAmount);
        }
        if (amount.compareTo(maxTransferLimit) > 0) {
            throw new InvalidTransactionException("Maximum transaction amount is ₹" + maxTransferLimit);
        }
    }

    private User resolveReceiver(SendMoneyRequest request) {
        if (request.getReceiverUpiId() != null) {
            return userRepository.findByUpiId(request.getReceiverUpiId())
                    .orElseThrow(() -> new ResourceNotFoundException("No user found with UPI ID: " + request.getReceiverUpiId()));
        }
        if (request.getReceiverPhone() != null) {
            return userRepository.findByPhone(request.getReceiverPhone())
                    .orElseThrow(() -> new ResourceNotFoundException("No user found with phone: " + request.getReceiverPhone()));
        }
        if (request.getReceiverEmail() != null) {
            return userRepository.findByEmail(request.getReceiverEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("No user found with email: " + request.getReceiverEmail()));
        }
        throw new InvalidTransactionException("At least one of receiverPhone, receiverEmail, or receiverUpiId is required");
    }
}
