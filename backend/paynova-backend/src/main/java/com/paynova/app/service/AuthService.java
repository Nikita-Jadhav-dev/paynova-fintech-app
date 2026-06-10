package com.paynova.app.service;

import com.paynova.app.dto.request.LoginRequest;
import com.paynova.app.dto.request.RegisterRequest;
import com.paynova.app.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
