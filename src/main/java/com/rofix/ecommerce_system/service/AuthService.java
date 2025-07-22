package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.security.request.LoginRequest;
import com.rofix.ecommerce_system.security.request.RegisterRequest;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    String register(RegisterRequest registerRequest);

    ResponseEntity<UserInfoResponse> login(LoginRequest loginRequest);
}