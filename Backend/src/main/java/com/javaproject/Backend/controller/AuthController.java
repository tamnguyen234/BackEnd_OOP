package com.javaproject.Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.LoginRequest;
import com.javaproject.Backend.dto.request.RegisterRequest;
import com.javaproject.Backend.dto.response.JwtResponse;
import com.javaproject.Backend.dto.response.RegisterResponse;
import com.javaproject.Backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // ===== Endpoint đăng ký user =====
    @PostMapping("/register")
    // @RequestBody: lấy dữ liệu JSON từ body request
    // @Valid: tự động validate dữ liệu theo annotation trong RegisterRequest
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
        // Gọi service register và trả về ResponseEntity chứa RegisterResponse
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

}
