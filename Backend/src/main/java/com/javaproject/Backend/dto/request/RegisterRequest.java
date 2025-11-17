package com.javaproject.Backend.dto.request;

import lombok.Data;

// --- RegisterRequest ---
@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
}

