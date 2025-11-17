package com.javaproject.Backend.dto.response;

import lombok.Data;

@Data
public class RegisterResponse {
    private Long userId;
    private String email;
    private String fullName;
    private String message = "User registered successfully";

    public RegisterResponse(Long userId, String email, String fullName) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
    }
}
