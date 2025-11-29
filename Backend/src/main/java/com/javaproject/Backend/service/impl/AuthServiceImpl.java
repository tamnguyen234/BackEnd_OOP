package com.javaproject.Backend.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.LoginRequest;
import com.javaproject.Backend.dto.request.RegisterRequest;
import com.javaproject.Backend.dto.response.JwtResponse;
import com.javaproject.Backend.dto.response.UserResponse;
import com.javaproject.Backend.events.UserCreatedEvent;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.AuthService;
import com.javaproject.Backend.util.JwtUtils;

import org.springframework.context.ApplicationEventPublisher;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

/**
 * Triển khai (Implementation) của AuthService, xử lý logic nghiệp vụ cho Xác thực (Authentication).
 * * @Service: Đánh dấu class này là Service Component của Spring.
 * * @RequiredArgsConstructor: Tự động tạo constructor với các trường final (Dependency Injection).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ApplicationEventPublisher eventPublisher;
    /**
     * Xử lý quá trình đăng ký: Kiểm tra tồn tại email, kiểm tra mật khẩu, mã hóa, lưu User.
     * Cuối cùng, phát sự kiện UserCreatedEvent để kích hoạt các hành động sau đăng ký
     * (ví dụ: tạo Budget mặc định).
     */
    @Override
    public UserResponse register(RegisterRequest request) {
        
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
        eventPublisher.publishEvent(new UserCreatedEvent(this, saved.getUserId()));
        return UserResponse.builder()
                // .userId(saved.getUserId())
                .email(saved.getEmail())
                .fullName(saved.getFullName())
                .build();
    }

    /**
     * Xử lý quá trình xác thực: Tìm User theo email, so sánh mật khẩu đã mã hóa.
     * Nếu thành công, tạo và trả về JWT Token.
     */
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
                // .userId(user.getUserId())
                .email(user.getEmail())
                .build();
    }
}
