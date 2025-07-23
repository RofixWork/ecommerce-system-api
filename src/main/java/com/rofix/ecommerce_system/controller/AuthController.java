package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.security.jwt.JwtUtils;
import com.rofix.ecommerce_system.security.request.LoginRequest;
import com.rofix.ecommerce_system.security.request.RegisterRequest;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Authentication", description = "User authentication APIs (Register, Login, Logout, Profile)")
@Slf4j
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Register a new user", description = "Creates a new account with username, email, and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        String status = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse(status, true));
    }

    @Operation(summary = "Login user", description = "Authenticate user and return JWT and user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(summary = "Get current authenticated user info", description = "Returns the user details based on JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token")
    })
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        UserInfoResponse userInfoResponse = authService.showMe(userDetails);
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }

    @Operation(summary = "Logout user", description = "Logs out the user by clearing the JWT cookie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<APIResponse> logout() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(
                new APIResponse("You have been logged out", true)
        );
    }
}

