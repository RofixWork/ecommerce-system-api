package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.security.request.LoginRequest;
import com.rofix.ecommerce_system.security.request.RegisterRequest;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        UserInfoResponse userInfoResponse = authService.showMe(userDetails);

        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        String status = authService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse(status, true));
    }
}
