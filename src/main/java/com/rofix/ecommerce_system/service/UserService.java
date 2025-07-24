package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import jakarta.validation.constraints.Min;

public interface UserService {
    PageListResponse<UserInfoResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserInfoResponse getUserBy(Long userId);

    String deleteUserBy(@Min(value = 1) Long userId, UserDetailsImpl userDetails);
}
