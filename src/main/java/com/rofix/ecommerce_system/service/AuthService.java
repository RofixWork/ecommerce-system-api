package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.security.request.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest registerRequest);
}
