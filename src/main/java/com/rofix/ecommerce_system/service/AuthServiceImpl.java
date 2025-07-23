package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.entity.Role;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.exception.base.UnauthorizedException;
import com.rofix.ecommerce_system.repository.RoleRepository;
import com.rofix.ecommerce_system.repository.UserRepository;
import com.rofix.ecommerce_system.security.jwt.JwtUtils;
import com.rofix.ecommerce_system.security.request.LoginRequest;
import com.rofix.ecommerce_system.security.request.RegisterRequest;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    @Override
    public String register(RegisterRequest registerRequest) {
        existsConflict(registerRequest);

        Set<Role> userRoles = getRoleSet(registerRequest);

        savedUser(registerRequest, userRoles);

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

        UserInfoResponse userInfoResponse = getUserInfoResponse(userDetails);

        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(userInfoResponse);
    }


    @Override
    public UserInfoResponse showMe(UserDetails userDetails) {
        return getUserInfoResponse(userDetails);
    }

    //    ---------------------------HELPERS------------------------------
    private void savedUser(RegisterRequest registerRequest, Set<Role> userRoles) {
        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        userRepository.save(user);
        log.info("User [{}] registered successfully", user.getUsername());
    }

    private Set<Role> getRoleSet(RegisterRequest registerRequest) {
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            Role customerRole = roleRepository.findByRoleName("customer").orElseThrow(() -> {
                log.error("Role 'CUSTOMER' not found");
                return new NotFoundException("Role 'CUSTOMER' not found");
            });

            return Set.of(customerRole);
        }

        return registerRequest.getRoles().stream().map(role -> roleRepository.findByRoleName(role.trim()).orElseThrow(() -> {
            log.error("Role [{}] not found", role);
            return new NotFoundException("Role '" + role.toUpperCase() + "' not found");
        })).collect(Collectors.toSet());
    }

    private void existsConflict(RegisterRequest req) {
        if (userRepository.existsByUsernameIgnoreCase(req.getUsername())) {
            throw new ConflictException("Username is already in use.");
        }
        if (userRepository.existsByEmailIgnoreCase(req.getEmail())) {
            throw new ConflictException("Email is already in use.");
        }
        if (userRepository.existsByPhoneIgnoreCase(req.getPhone())) {
            throw new ConflictException("Phone is already in use.");
        }
    }

    private UserInfoResponse getUserInfoResponse(UserDetails user) {
        UserInfoResponse userInfoResponse = modelMapper.map(user, UserInfoResponse.class);
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        userInfoResponse.setRoles(roles);
        return userInfoResponse;
    }
}
