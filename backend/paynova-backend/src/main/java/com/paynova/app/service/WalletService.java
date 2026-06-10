package com.paynova.app.service;

import com.paynova.app.dto.request.AddMoneyRequest;
import com.paynova.app.dto.request.SendMoneyRequest;
import com.paynova.app.dto.response.TransactionResponse;
import com.paynova.app.dto.response.WalletResponse;

public interface WalletService {
    WalletResponse getMyWallet();
    TransactionResponse addMoney(AddMoneyRequest request);
    TransactionResponse sendMoney(SendMoneyRequest request);
}
