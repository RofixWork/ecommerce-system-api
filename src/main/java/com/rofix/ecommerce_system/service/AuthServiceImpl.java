package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.entity.Role;
import com.rofix.ecommerce_system.exception.base.UnauthorizedException;
import com.rofix.ecommerce_system.security.jwt.JwtUtils;
import com.rofix.ecommerce_system.security.request.LoginRequest;
import com.rofix.ecommerce_system.security.request.RegisterRequest;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.utils.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthHelper authHelper;

    @Transactional
    @Override
    public String register(RegisterRequest registerRequest) {
        authHelper.existsConflict(registerRequest);

        Set<Role> userRoles = authHelper.getRoleSet(registerRequest);

        authHelper.savedUser(registerRequest, userRoles);

        return "User registration completed";
    }

    @Transactional
    @Override
    public ResponseEntity<UserInfoResponse> login(LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException ex) {
            log.info("Invalid username or password", ex);
            throw new UnauthorizedException("Invalid credentials");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserInfoResponse userInfoResponse = authHelper.getUserInfoResponse(userDetails);

        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(userInfoResponse);
    }


    @Override
    public UserInfoResponse showMe(UserDetails userDetails) {
        return authHelper.getUserInfoResponse(userDetails);
    }
}
