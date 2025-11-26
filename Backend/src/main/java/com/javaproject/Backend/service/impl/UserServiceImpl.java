package com.javaproject.Backend.service.impl;

import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.response.UserResponse;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Service thao tác trực tiếp với User entity
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    // Sử dụng Jwt lấy user_ID của lient trong token
    @Override
    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        throw new AccessDeniedException("User ID not found or not authenticated.");
    }

    private final UserRepository userRepository;

    // ==== Lấy thông tin một user = userId ====
    @Override
    public UserResponse getUserById(Long userId) {
        // Tìm user theo ID, nếu không tồn tại thì ném exception
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse();
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        return response;
    }

    // xoá người dùng:
    @Override
    public UserResponse deleteUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    // Cập nhập thông tin người dùng:
    @Override
    public UserResponse updateUser(Long userId, UserResponse request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    // Tìm user = email
    @Override
    public Optional<User> findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

}
