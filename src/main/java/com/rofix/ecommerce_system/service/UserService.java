package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import jakarta.validation.constraints.Min;

public interface UserService {
    PageListResponse<UserInfoResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
