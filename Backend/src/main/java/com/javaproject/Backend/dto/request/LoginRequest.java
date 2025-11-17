package com.javaproject.Backend.dto.request;

// DTO dùng để nhận dữ liệu Đăng nhập (Authentication)
public class LoginRequest {
    private String email;
    private String password;

    // Constructors (Bạn có thể bỏ qua nếu dùng thư viện như Lombok)
    public LoginRequest() {
    }

    // Getters và Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}