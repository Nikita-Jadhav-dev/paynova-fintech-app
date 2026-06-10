package com.paynova.app.service;

import com.paynova.app.dto.request.AddBankAccountRequest;
import com.paynova.app.dto.response.BankAccountResponse;

import java.util.List;

public interface BankAccountService {
    BankAccountResponse addBankAccount(AddBankAccountRequest request);
    List<BankAccountResponse> getMyBankAccounts();
    BankAccountResponse getBankAccountById(Long accountId);
    BankAccountResponse setPrimary(Long accountId);
    void deleteBankAccount(Long accountId);
}
