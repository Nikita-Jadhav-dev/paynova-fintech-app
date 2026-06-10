package com.paynova.app.controller;

import com.paynova.app.dto.request.AddMoneyRequest;
import com.paynova.app.dto.request.SendMoneyRequest;
import com.paynova.app.dto.response.ApiResponse;
import com.paynova.app.dto.response.TransactionResponse;
import com.paynova.app.dto.response.WalletResponse;
import com.paynova.app.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@Tag(name = "Wallet", description = "Wallet balance, add money, send money")
@SecurityRequirement(name = "bearerAuth")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance")
    @Operation(summary = "Check wallet balance")
    public ResponseEntity<ApiResponse<WalletResponse>> getBalance() {
        return ResponseEntity.ok(ApiResponse.success(walletService.getMyWallet()));
    }

    @PostMapping("/add-money")
    @Operation(summary = "Add money to wallet from bank account")
    public ResponseEntity<ApiResponse<TransactionResponse>> addMoney(
            @Valid @RequestBody AddMoneyRequest request) {
        TransactionResponse tx = walletService.addMoney(request);
        return ResponseEntity.ok(ApiResponse.success("Money added successfully", tx));
    }

    @PostMapping("/send-money")
    @Operation(summary = "Send money to another user (by phone, email, or UPI ID)")
    public ResponseEntity<ApiResponse<TransactionResponse>> sendMoney(
            @Valid @RequestBody SendMoneyRequest request) {
        TransactionResponse tx = walletService.sendMoney(request);
        return ResponseEntity.ok(ApiResponse.success("Money sent successfully", tx));
    }
}
