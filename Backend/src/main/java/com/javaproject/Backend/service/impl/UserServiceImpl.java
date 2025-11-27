package com.javaproject.Backend.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.update.UserUpdateRequest;
import com.javaproject.Backend.dto.response.UserResponse;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Lấy danh sách tất cả userID:
    @Override
    public List<Long> getAllUserIds() {
        return userRepository.findAll().stream().map(User::getUserId).collect(Collectors.toList());
    }

    // ==== Lấy userId hiện tại từ JWT ====
    @Override
    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        throw new AccessDeniedException("User ID not found or not authenticated.");
    }

    // ==== Lấy thông tin user theo ID ====
    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    // ==== Cập nhật thông tin user ====
    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cập nhật password nếu có
        if (request.getNewPassword() != null) {
            if (request.getOldPassword() == null ||
                    !passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            if (!request.getNewPassword().equals(request.getCheckPassword())) {
                throw new IllegalArgumentException("New password and confirmation do not match");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        }

        // Cập nhật fullName nếu có
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }

        // Lưu user
        userRepository.save(user);

        return UserResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    // ==== Xoá user ====
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    // ==== Tìm user theo email ====
    @Override
    public Optional<User> findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public UserResponse updateUser(Long userId, UserResponse request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }
}
