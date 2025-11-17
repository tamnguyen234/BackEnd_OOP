package com.javaproject.Backend.dto.response;

// DTO dùng để trả về JWT Token sau khi đăng nhập thành công
public class JwtResponse {
    private String token;
    private String type = "Bearer"; // Loại token mặc định
    private Long userId; // ID người dùng để Client lưu trữ
    private String email; // Email người dùng

    // Constructor đầy đủ
    public JwtResponse(String token, Long userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    // Getters và Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}