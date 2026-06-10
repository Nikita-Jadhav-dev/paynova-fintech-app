package com.paynova.app.service.impl;

import com.paynova.app.dto.request.ChangePasswordRequest;
import com.paynova.app.dto.request.UpdateProfileRequest;
import com.paynova.app.dto.response.UserResponse;
import com.paynova.app.entity.User;
import com.paynova.app.entity.enums.UserStatus;
import com.paynova.app.exception.DuplicateResourceException;
import com.paynova.app.exception.ResourceNotFoundException;
import com.paynova.app.repository.UserRepository;
import com.paynova.app.service.UserService;
import com.paynova.app.util.EntityMapper;
import com.paynova.app.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;
    private final EntityMapper mapper;

    @Override
    public UserResponse getMyProfile() {
        return mapper.toUserResponse(securityUtils.getCurrentUser());
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = securityUtils.getCurrentUser();

        if (request.getName() != null) user.setName(request.getName());

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new DuplicateResourceException("Phone number already in use: " + request.getPhone());
            }
            user.setPhone(request.getPhone());
            user.setUpiId(request.getPhone() + "@paynova");
        }

        if (request.getProfileImageUrl() != null) user.setProfileImageUrl(request.getProfileImageUrl());

        user = userRepository.save(user);
        log.info("Profile updated for user: {}", user.getId());
        return mapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = securityUtils.getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getId());
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return mapper.toUserResponse(user);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(mapper::toUserResponse);
    }

    @Override
    @Transactional
    public UserResponse blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setStatus(UserStatus.BLOCKED);
        user = userRepository.save(user);
        log.info("User blocked: {}", userId);
        return mapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        log.info("User unblocked: {}", userId);
        return mapper.toUserResponse(user);
    }

    @Override
    public Page<UserResponse> searchUsers(String query, Pageable pageable) {
        return userRepository.searchUsers(query, pageable).map(mapper::toUserResponse);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);
    }
}
