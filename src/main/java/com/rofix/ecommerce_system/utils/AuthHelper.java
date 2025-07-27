package com.rofix.ecommerce_system.utils;

import com.rofix.ecommerce_system.entity.Role;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.RoleRepository;
import com.rofix.ecommerce_system.repository.UserRepository;
import com.rofix.ecommerce_system.security.request.RegisterRequest;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHelper {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void savedUser(RegisterRequest registerRequest, Set<Role> userRoles) {
        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        userRepository.save(user);
        log.info("User [{}] registered successfully", user.getUsername());
    }

    public Set<Role> getRoleSet(RegisterRequest registerRequest) {
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

    public void existsConflict(RegisterRequest req) {
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

    public UserInfoResponse getUserInfoResponse(UserDetails user) {
        UserInfoResponse userInfoResponse = modelMapper.map(user, UserInfoResponse.class);
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        userInfoResponse.setRoles(roles);
        return userInfoResponse;
    }
}
