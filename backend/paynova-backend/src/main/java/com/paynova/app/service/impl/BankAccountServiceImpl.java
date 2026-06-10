package com.paynova.app.service.impl;

import com.paynova.app.dto.request.AddBankAccountRequest;
import com.paynova.app.dto.response.BankAccountResponse;
import com.paynova.app.entity.BankAccount;
import com.paynova.app.entity.User;
import com.paynova.app.exception.DuplicateResourceException;
import com.paynova.app.exception.ResourceNotFoundException;
import com.paynova.app.repository.BankAccountRepository;
import com.paynova.app.service.BankAccountService;
import com.paynova.app.util.EntityMapper;
import com.paynova.app.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final SecurityUtils securityUtils;
    private final EntityMapper mapper;

    @Override
    @Transactional
    public BankAccountResponse addBankAccount(AddBankAccountRequest request) {
        User user = securityUtils.getCurrentUser();

        if (bankAccountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new DuplicateResourceException("Bank account already registered: " + request.getAccountNumber());
        }

        // If this is the first account or isPrimary requested, handle existing primary
        boolean shouldBePrimary = request.getIsPrimary()
                || bankAccountRepository.countByUserId(user.getId()) == 0;

        if (shouldBePrimary) {
            // Unset any existing primary
            bankAccountRepository.findByUserIdAndIsPrimaryTrue(user.getId())
                    .ifPresent(existing -> {
                        existing.setIsPrimary(false);
                        bankAccountRepository.save(existing);
                    });
        }

        BankAccount account = BankAccount.builder()
                .user(user)
                .accountNumber(request.getAccountNumber())
                .ifscCode(request.getIfscCode().toUpperCase())
                .bankName(request.getBankName())
                .accountHolderName(request.getAccountHolderName())
                .isPrimary(shouldBePrimary)
                .isVerified(false)
                .build();

        account = bankAccountRepository.save(account);
        log.info("Bank account added for user {}: {}", user.getId(), account.getId());
        return mapper.toBankAccountResponse(account);
    }

    @Override
    public List<BankAccountResponse> getMyBankAccounts() {
        Long userId = securityUtils.getCurrentUserId();
        return bankAccountRepository.findByUserId(userId)
                .stream()
                .map(mapper::toBankAccountResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountResponse getBankAccountById(Long accountId) {
        User user = securityUtils.getCurrentUser();
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("BankAccount", "id", accountId));

        // Non-admin users can only view their own accounts
        if (!securityUtils.isCurrentUserAdmin() && !account.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("BankAccount", "id", accountId);
        }
        return mapper.toBankAccountResponse(account);
    }

    @Override
    @Transactional
    public BankAccountResponse setPrimary(Long accountId) {
        User user = securityUtils.getCurrentUser();
        BankAccount newPrimary = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("BankAccount", "id", accountId));

        if (!newPrimary.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("BankAccount", "id", accountId);
        }

        // Unset old primary
        bankAccountRepository.findByUserIdAndIsPrimaryTrue(user.getId())
                .ifPresent(existing -> {
                    existing.setIsPrimary(false);
                    bankAccountRepository.save(existing);
                });

        newPrimary.setIsPrimary(true);
        newPrimary = bankAccountRepository.save(newPrimary);
        return mapper.toBankAccountResponse(newPrimary);
    }

    @Override
    @Transactional
    public void deleteBankAccount(Long accountId) {
        User user = securityUtils.getCurrentUser();
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("BankAccount", "id", accountId));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("BankAccount", "id", accountId);
        }
        bankAccountRepository.delete(account);
        log.info("Bank account {} deleted for user {}", accountId, user.getId());
    }
}
