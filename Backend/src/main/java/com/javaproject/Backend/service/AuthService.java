package com.javaproject.Backend.service;

import com.javaproject.Backend.dto.request.LoginRequest;
import com.javaproject.Backend.dto.request.RegisterRequest;
import com.javaproject.Backend.dto.response.JwtResponse;
import com.javaproject.Backend.dto.response.UserResponse;/**
 * Interface định nghĩa các dịch vụ (Service) liên quan đến Xác thực (Authentication) người dùng.
 * * Bao gồm các chức năng cốt lõi như Đăng ký tài khoản mới và Đăng nhập.
 */
public interface AuthService {
    /**
     * Xử lý yêu cầu Đăng ký tài khoản người dùng mới.
     * * Phương thức này chịu trách nhiệm: Tạo đối tượng User, mã hóa mật khẩu, lưu vào DB, 
     * và publish sự kiện UserCreatedEvent.
     * @param request Dữ liệu đăng ký từ client (email, password, fullname).
     * @return UserResponse chứa thông tin cơ bản của người dùng vừa được tạo.
     */
    UserResponse register(RegisterRequest request);

    /**
     * Xử lý yêu cầu Đăng nhập và Xác thực người dùng.
     * * Phương thức này chịu trách nhiệm: Kiểm tra email/password, tạo JWT Token
     * nếu xác thực thành công.
     * @param request Dữ liệu đăng nhập từ client (email, password).
     * @return JwtResponse chứa JWT Token và thông tin cơ bản của người dùng (nếu thành công).
     */
    JwtResponse authenticate(LoginRequest request);
}
