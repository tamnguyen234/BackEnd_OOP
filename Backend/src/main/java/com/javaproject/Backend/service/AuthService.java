package com.javaproject.Backend.service;

import com.javaproject.Backend.dto.request.LoginRequest;
import com.javaproject.Backend.dto.request.RegisterRequest;
import com.javaproject.Backend.dto.response.JwtResponse;
import com.javaproject.Backend.dto.response.UserResponse;

public interface AuthService {
    // ==== Đăng kí ====
    UserResponse register(RegisterRequest request);

    // ==== Đăng nhập ====
    JwtResponse authenticate(LoginRequest request);
}
