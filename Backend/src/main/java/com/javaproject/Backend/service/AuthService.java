package com.javaproject.Backend.service;

import com.javaproject.Backend.dto.request.LoginRequest;
import com.javaproject.Backend.dto.request.RegisterRequest;
import com.javaproject.Backend.dto.response.JwtResponse;
import com.javaproject.Backend.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    JwtResponse authenticate(LoginRequest request);
}
