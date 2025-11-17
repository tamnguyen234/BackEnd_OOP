package com.javaproject.Backend.dto.response;

// DTO dùng để trả về JWT Token sau khi đăng nhập thành công
import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;

    public JwtResponse(String token, Long userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }
}