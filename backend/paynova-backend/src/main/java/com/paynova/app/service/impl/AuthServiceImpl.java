package com.paynova.app.service.impl;

import com.paynova.app.dto.request.LoginRequest;
import com.paynova.app.dto.request.RegisterRequest;
import com.paynova.app.dto.response.AuthResponse;
import com.paynova.app.dto.response.UserResponse;
import com.paynova.app.entity.QRPayment;
import com.paynova.app.entity.User;
import com.paynova.app.entity.Wallet;
import com.paynova.app.entity.enums.Role;
import com.paynova.app.entity.enums.UserStatus;
import com.paynova.app.exception.DuplicateResourceException;
import com.paynova.app.repository.QRPaymentRepository;
import com.paynova.app.repository.UserRepository;
import com.paynova.app.repository.WalletRepository;
import com.paynova.app.security.JwtService;
import com.paynova.app.service.AuthService;
import com.paynova.app.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final QRPaymentRepository qrPaymentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final EntityMapper mapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Duplicate checks
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + request.getEmail());
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number is already registered: " + request.getPhone());
        }

        // Build and persist user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .upiId(generateUpiId(request.getPhone()))
                .build();
        user = userRepository.save(user);

        // Create wallet for the new user
        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .isActive(true)
                .build();
        walletRepository.save(wallet);

        // Auto-generate QR code
        QRPayment qr = QRPayment.builder()
                .user(user)
                .isActive(true)
                .build();
        qrPaymentRepository.save(qr);

        log.info("New user registered: {} ({})", user.getEmail(), user.getId());

        String token = generateToken(user.getEmail());
        UserResponse userResponse = mapper.toUserResponse(user);
        return AuthResponse.of(token, jwtService.getExpirationMs(), userResponse);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Throws BadCredentialsException / DisabledException automatically
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        log.info("User logged in: {}", user.getEmail());

        String token = generateToken(user.getEmail());
        UserResponse userResponse = mapper.toUserResponse(user);
        return AuthResponse.of(token, jwtService.getExpirationMs(), userResponse);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private String generateToken(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtService.generateToken(userDetails);
    }

    private String generateUpiId(String phone) {
        return phone + "@paynova";
    }
}
