package com.paynova.app.service;

import com.paynova.app.dto.request.QRPayRequest;
import com.paynova.app.dto.request.RefundRequest;
import com.paynova.app.dto.response.PagedResponse;
import com.paynova.app.dto.response.TransactionResponse;
import com.paynova.app.entity.enums.TransactionType;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    PagedResponse<TransactionResponse> getMyTransactions(Pageable pageable);
    PagedResponse<TransactionResponse> getMyTransactionsByType(TransactionType type, Pageable pageable);
    TransactionResponse getTransactionById(String transactionId);
    TransactionResponse payByQR(QRPayRequest request);
    TransactionResponse refundTransaction(RefundRequest request);
    PagedResponse<TransactionResponse> getAllTransactions(Pageable pageable);
}
