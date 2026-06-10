package com.paynova.app.controller;

import com.paynova.app.dto.request.QRPayRequest;
import com.paynova.app.dto.response.ApiResponse;
import com.paynova.app.dto.response.PagedResponse;
import com.paynova.app.dto.response.TransactionResponse;
import com.paynova.app.entity.enums.TransactionType;
import com.paynova.app.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction history and QR payments")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Get my transaction history (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(transactionService.getMyTransactions(pageable)));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter my transactions by type")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> filterByType(
            @RequestParam TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(transactionService.getMyTransactionsByType(type, pageable)));
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get transaction details by ID")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(@PathVariable String transactionId) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionById(transactionId)));
    }

    @PostMapping("/qr-pay")
    @Operation(summary = "Pay via QR code scan simulation")
    public ResponseEntity<ApiResponse<TransactionResponse>> payByQR(
            @Valid @RequestBody QRPayRequest request) {
        TransactionResponse tx = transactionService.payByQR(request);
        return ResponseEntity.ok(ApiResponse.success("QR payment successful", tx));
    }
}
