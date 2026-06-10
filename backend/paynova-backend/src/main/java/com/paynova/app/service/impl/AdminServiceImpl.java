package com.paynova.app.service.impl;

import com.paynova.app.dto.response.AdminStatsResponse;
import com.paynova.app.entity.enums.TransactionStatus;
import com.paynova.app.entity.enums.TransactionType;
import com.paynova.app.entity.enums.UserStatus;
import com.paynova.app.repository.TransactionRepository;
import com.paynova.app.repository.UserRepository;
import com.paynova.app.repository.WalletRepository;
import com.paynova.app.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminStatsResponse getSystemStats() {
        long totalUsers    = userRepository.count();
        long activeUsers   = userRepository.countByStatus(UserStatus.ACTIVE);
        long blockedUsers  = userRepository.countByStatus(UserStatus.BLOCKED);

        long totalTx       = transactionRepository.count();
        long successTx     = transactionRepository.countByStatus(TransactionStatus.SUCCESS);
        long failedTx      = transactionRepository.countByStatus(TransactionStatus.FAILED);

        BigDecimal totalTransferred = transactionRepository.sumByTypeAndSuccess(TransactionType.SEND);
        BigDecimal totalWalletBal   = walletRepository.getTotalWalletBalance();

        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        long txToday = transactionRepository.countTransactionsSince(startOfToday);

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay();
        long newUsersThisMonth = userRepository.count() - userRepository.countByStatus(UserStatus.BLOCKED); // simplified

        return AdminStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .blockedUsers(blockedUsers)
                .totalTransactions(totalTx)
                .successfulTransactions(successTx)
                .failedTransactions(failedTx)
                .totalMoneyTransferred(totalTransferred != null ? totalTransferred : BigDecimal.ZERO)
                .totalWalletBalance(totalWalletBal != null ? totalWalletBal : BigDecimal.ZERO)
                .transactionsToday(txToday)
                .newUsersThisMonth(newUsersThisMonth)
                .build();
    }
}
