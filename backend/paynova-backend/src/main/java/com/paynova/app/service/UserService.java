package com.paynova.app.service;

import com.paynova.app.dto.request.ChangePasswordRequest;
import com.paynova.app.dto.request.UpdateProfileRequest;
import com.paynova.app.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse getMyProfile();
    UserResponse updateProfile(UpdateProfileRequest request);
    void changePassword(ChangePasswordRequest request);
    UserResponse getUserById(Long userId);
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse blockUser(Long userId);
    UserResponse unblockUser(Long userId);
    Page<UserResponse> searchUsers(String query, Pageable pageable);
    void deleteUser(Long userId);
}
