package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.repository.UserRepository;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.utils.EntityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EntityHelper entityHelper;

    @Override
    public PageListResponse<UserInfoResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        entityHelper.checkOrderField(sortBy, AppConstants.ALLOWED_USER_ORDERING_FIELDS);
        Pageable pageable = entityHelper.getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> users = userPage.getContent();

        List<UserInfoResponse> userInfoResponseDTOS = users.stream()
                .map(user -> {
                    UserInfoResponse userInfoResponse = modelMapper.map(user, UserInfoResponse.class);
                    Set<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
                    userInfoResponse.setRoles(roles);
                    return userInfoResponse;
                })
                .toList();

        boolean isEmpty = userPage.isEmpty();
        return new PageListResponse<>(
                userInfoResponseDTOS,
                isEmpty ? 0 : userPage.getSize(),
                isEmpty ? 0 : userPage.getNumber() + 1,
                isEmpty ? 0 : userPage.getTotalPages(),
                isEmpty ? 0 : userPage.getTotalElements(),
                userPage.isLast()
        );
    }
}
