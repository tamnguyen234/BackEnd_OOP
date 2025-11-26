package com.javaproject.Backend.service;

import java.util.Optional;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.update.UserUpdateRequest;
import com.javaproject.Backend.dto.response.UserResponse;

/**
 * Interface định nghĩa các nghiệp vụ liên quan đến User
 */
public interface UserService {

    // Lấy ID trong token (sử dụngJwt)
    Long getCurrentUserId();

    // Lấy thông tin người dùng:
    UserResponse getUserById(Long userId);

    // Xoá người dùng:
    void deleteUser(Long userId);

    // Cập nhập thông tin người dùng:
    UserResponse updateUser(Long userId, UserResponse request);

    // Tìm user = email, Optional: trả về rỗng nếu k tìm thấy
    Optional<User> findByEmail(String email);

    UserResponse updateUser(Long userId, UserUpdateRequest request);
}