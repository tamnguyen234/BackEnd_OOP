package com.javaproject.Backend.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.LoginRequest;
import com.javaproject.Backend.dto.request.RegisterRequest;
import com.javaproject.Backend.dto.response.JwtResponse;
import com.javaproject.Backend.dto.response.RegisterResponse;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.AuthService;
import com.javaproject.Backend.util.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // ==== Đăng kí tài khoản =====
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // check existed
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already registered");
        });
        // Check nhập lại mật khẩu
        if (!request.getPassword().equals(request.getCheckpassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .build();
        User saved = userRepository.save(user);
        return RegisterResponse.builder()
                .userId(saved.getUserId())
                .email(saved.getEmail())
                .fullName(saved.getFullName())
                .build();
    }

    // ===== Đăng nhập =====
    @Override
    public JwtResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // so sánh mật khẩu nhập vào đã mã hóa với pass trong dữ liệu
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtils.generateToken(user.getEmail(), user.getUserId());
        return JwtResponse.builder()
                .token(token)
                .userId(user.getUserId())
                .email(user.getEmail())
                .build();
    }
}
