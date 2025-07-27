package com.rofix.ecommerce_system.utils;

import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserHelper {
    private final ModelMapper modelMapper;

    public UserInfoResponse getUserInfoResponse(User user) {
        UserInfoResponse userInfoResponse = modelMapper.map(user, UserInfoResponse.class);
        userInfoResponse.setRoles(getRoles(user));
        return userInfoResponse;
    }

    public Set<String> getRoles(User user) {
        return user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
    }
}
