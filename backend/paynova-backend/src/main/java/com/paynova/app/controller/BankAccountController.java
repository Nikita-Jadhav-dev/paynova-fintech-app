package com.paynova.app.controller;

import com.paynova.app.dto.request.AddBankAccountRequest;
import com.paynova.app.dto.response.ApiResponse;
import com.paynova.app.dto.response.BankAccountResponse;
import com.paynova.app.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-accounts")
@RequiredArgsConstructor
@Tag(name = "Bank Accounts", description = "Add and manage linked bank accounts")
@SecurityRequirement(name = "bearerAuth")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    @Operation(summary = "Link a new bank account")
    public ResponseEntity<ApiResponse<BankAccountResponse>> addBankAccount(
            @Valid @RequestBody AddBankAccountRequest request) {
        BankAccountResponse account = bankAccountService.addBankAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bank account added successfully", account));
    }

    @GetMapping
    @Operation(summary = "Get all my linked bank accounts")
    public ResponseEntity<ApiResponse<List<BankAccountResponse>>> getMyAccounts() {
        return ResponseEntity.ok(ApiResponse.success(bankAccountService.getMyBankAccounts()));
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get a specific bank account by ID")
    public ResponseEntity<ApiResponse<BankAccountResponse>> getAccountById(@PathVariable Long accountId) {
        return ResponseEntity.ok(ApiResponse.success(bankAccountService.getBankAccountById(accountId)));
    }

    @PatchMapping("/{accountId}/set-primary")
    @Operation(summary = "Set a bank account as primary")
    public ResponseEntity<ApiResponse<BankAccountResponse>> setPrimary(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                ApiResponse.success("Primary account updated", bankAccountService.setPrimary(accountId)));
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "Remove a linked bank account")
    public ResponseEntity<ApiResponse<Void>> deleteBankAccount(@PathVariable Long accountId) {
        bankAccountService.deleteBankAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success("Bank account removed", null));
    }
}
