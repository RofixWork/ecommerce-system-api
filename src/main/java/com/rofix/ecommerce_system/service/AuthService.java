package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.security.request.LoginRequest;
import com.rofix.ecommerce_system.security.request.RegisterRequest;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    @Transactional
    String register(RegisterRequest registerRequest);

    @Transactional
    ResponseEntity<UserInfoResponse> login(LoginRequest loginRequest);

    UserInfoResponse showMe(UserDetails userDetails);
}