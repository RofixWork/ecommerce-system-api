package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.repository.UserRepository;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.utils.EntityHelper;
import com.rofix.ecommerce_system.utils.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EntityHelper entityHelper;
    private final UserHelper userHelper;

    @Override
    public PageListResponse<UserInfoResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        entityHelper.checkOrderField(sortBy, AppConstants.ALLOWED_USER_ORDERING_FIELDS);
        Pageable pageable = entityHelper.getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> users = userPage.getContent();

        List<UserInfoResponse> userInfoResponseDTOS = users.stream()
                .map(userHelper::getUserInfoResponse)
                .toList();

        return entityHelper.getPageListResponse(userPage, userInfoResponseDTOS);
    }

    @Override
    public UserInfoResponse getUserBy(Long userId) {
        User user = entityHelper.getUserOrThrow(userId);
        return userHelper.getUserInfoResponse(user);
    }

    @Override
    public String deleteUserBy(Long userId, UserDetailsImpl userDetails) {
        User user = entityHelper.getUserOrThrow(userId);

        if (user.getId().equals(userDetails.getId())) {
            log.warn("User ID: {} attempted to delete their own active account.", userDetails.getId());
            throw new BadRequestException("Logged-in users cannot delete their own account.");
        }

        userRepository.delete(user);
        log.info("User '{}' has been deleted successfully.", user.getUsername());
        return "User '" + user.getUsername() + "' has been deleted successfully.";
    }
}
