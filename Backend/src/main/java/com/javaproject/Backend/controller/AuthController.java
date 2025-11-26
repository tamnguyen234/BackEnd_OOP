package com.javaproject.Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.LoginRequest;
import com.javaproject.Backend.dto.request.RegisterRequest;
import com.javaproject.Backend.dto.request.update.UserUpdateRequest;
import com.javaproject.Backend.dto.response.JwtResponse;
import com.javaproject.Backend.dto.response.UserResponse;
import com.javaproject.Backend.service.AuthService;
import com.javaproject.Backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    // ===== Endpoint đăng ký user =====
    @PostMapping("/register")
    // @RequestBody: lấy dữ liệu JSON từ body request
    // @Valid: tự động validate dữ liệu theo annotation trong RegisterRequest
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
        // Gọi service register và trả về ResponseEntity chứa UserResponse
    }

    // ===== Endpoint đăng nhập user =====
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest req) {
        JwtResponse token = authService.authenticate(req);
        // Gọi service xác thực và trả token JWT
        return ResponseEntity.ok(token);
    }

    // Đăng xuất
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Backend không làm gì, chỉ trả OK, FE xoá token và chuyển về trang login
        return ResponseEntity.ok().build();
    }

    // ==== Lấy thông tin user hiện tại ====
    @GetMapping("/in4")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = userService.getCurrentUserId();
        UserResponse userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    // ==== Cập nhật thông tin user (fullname / password) ====
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateCurrentUser(@RequestBody UserUpdateRequest request) {
        Long userId = userService.getCurrentUserId();
        UserResponse updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    // ==== Xoá user hiện tại ====
    @DeleteMapping("/delete")
    public ResponseEntity<UserResponse> deleteCurrentUser() {
        Long userId = userService.getCurrentUserId();
        UserResponse deletedUser = userService.deleteUser(userId);
        return ResponseEntity.ok(deletedUser);
    }

}
