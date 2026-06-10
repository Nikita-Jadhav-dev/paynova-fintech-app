package com.paynova.app.controller;

import com.paynova.app.dto.response.ApiResponse;
import com.paynova.app.dto.response.QRPaymentResponse;
import com.paynova.app.service.QRPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
@Tag(name = "QR Code", description = "Manage and look up QR payment codes")
@SecurityRequirement(name = "bearerAuth")
public class QRPaymentController {

    private final QRPaymentService qrPaymentService;

    @GetMapping("/my-code")
    @Operation(summary = "Get my QR payment code")
    public ResponseEntity<ApiResponse<QRPaymentResponse>> getMyQRCode() {
        return ResponseEntity.ok(ApiResponse.success(qrPaymentService.getMyQRCode()));
    }

    @PostMapping("/regenerate")
    @Operation(summary = "Regenerate my QR code (invalidates the old one)")
    public ResponseEntity<ApiResponse<QRPaymentResponse>> regenerate() {
        return ResponseEntity.ok(
                ApiResponse.success("QR code regenerated", qrPaymentService.regenerateQRCode()));
    }

    @GetMapping("/lookup/{qrCode}")
    @Operation(summary = "Lookup a QR code to get recipient info before paying")
    public ResponseEntity<ApiResponse<QRPaymentResponse>> lookup(@PathVariable String qrCode) {
        return ResponseEntity.ok(ApiResponse.success(qrPaymentService.lookupQRCode(qrCode)));
    }
}
