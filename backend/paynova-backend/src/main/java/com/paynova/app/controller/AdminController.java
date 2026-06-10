package com.paynova.app.controller;

import com.paynova.app.dto.request.RefundRequest;
import com.paynova.app.dto.response.*;
import com.paynova.app.service.AdminService;
import com.paynova.app.service.TransactionService;
import com.paynova.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin-only endpoints for platform management")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final AdminService adminService;

    // ─── System Stats ─────────────────────────────────────────────────────────

    @GetMapping("/stats")
    @Operation(summary = "Get platform-wide statistics")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getSystemStats()));
    }

    // ─── User Management ──────────────────────────────────────────────────────

    @GetMapping("/users")
    @Operation(summary = "List all users (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(pageable)));
    }

    @GetMapping("/users/search")
    @Operation(summary = "Search users by name, email or phone")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(userService.searchUsers(query, pageable)));
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user details by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(userId)));
    }

    @PatchMapping("/users/{userId}/block")
    @Operation(summary = "Block a user account")
    public ResponseEntity<ApiResponse<UserResponse>> blockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("User blocked", userService.blockUser(userId)));
    }

    @PatchMapping("/users/{userId}/unblock")
    @Operation(summary = "Unblock a user account")
    public ResponseEntity<ApiResponse<UserResponse>> unblockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("User unblocked", userService.unblockUser(userId)));
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Permanently delete a user account")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted", null));
    }

    // ─── Transaction Management ───────────────────────────────────────────────

    @GetMapping("/transactions")
    @Operation(summary = "List all transactions across the platform (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(transactionService.getAllTransactions(pageable)));
    }

    @GetMapping("/transactions/{transactionId}")
    @Operation(summary = "Get a specific transaction by its ID")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(
            @PathVariable String transactionId) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionById(transactionId)));
    }

    @PostMapping("/transactions/refund")
    @Operation(summary = "Refund a successful transaction")
    public ResponseEntity<ApiResponse<TransactionResponse>> refund(
            @Valid @RequestBody RefundRequest request) {
        TransactionResponse tx = transactionService.refundTransaction(request);
        return ResponseEntity.ok(ApiResponse.success("Transaction refunded successfully", tx));
    }
}
