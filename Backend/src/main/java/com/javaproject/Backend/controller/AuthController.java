package com.javaproject.Backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.LoginRequest;
import com.javaproject.Backend.dto.request.RegisterRequest;
import com.javaproject.Backend.dto.request.update.UserUpdateRequest;
import com.javaproject.Backend.dto.response.JwtResponse;
import com.javaproject.Backend.dto.response.UserResponse;
import com.javaproject.Backend.service.AuthService;
import com.javaproject.Backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các yêu cầu liên quan đến Xác thực (Authentication) và Quản lý 
 * hồ sơ người dùng hiện tại.
 * * Bao gồm các endpoint: Đăng ký, Đăng nhập, Đăng xuất, Lấy/Cập nhật/Xóa thông tin user.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    // // Inject AuthService để xử lý logic đăng ký/đăng nhập
    private final AuthService authService;
    // Inject UserService để xử lý logic quản lý thông tin người dùng
    private final UserService userService;

    /**
     * Endpoint xử lú yêu cầu đăng kí người dùng mới
     * PATH: /api/auth/register
     * @param req Dữ liệu đăng ký từ client (@Valid tự động kích hoạt validation)
     * @RequestBody: lấy dữ liệu JSON từ body request
     * @return ResponseEntity chứa UserResponse của người dùng vừa được tạo.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    /**
     * Endpoint xử lý yêu cầu đăng nhập.
     * PATH: /api/auth/login
     * @param req Dữ liệu đăng nhập (username/email và password).
     * @return ResponseEntity chứa JwtResponse, bao gồm JWT Token nếu xác thực thành công.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest req) {
        JwtResponse token = authService.authenticate(req);
        return ResponseEntity.ok(token);
    }
 
    /**
     * Endpoint xử lý yêu cầu đăng xuất.
     * PATH: /api/auth/logout
     * * Trong kiến trúc JWT (stateless), backend chỉ trả về 200 OK.
     * * Trách nhiệm xóa token khỏi client (localStorage/Cookie) thuộc về frontend.
     * @return ResponseEntity<Void> với trạng thái HTTP 200 OK.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint lấy thông tin chi tiết của người dùng hiện tại.
     * PATH: /api/auth/in4
     * * Yêu cầu phải được xác thực (Authorization header hợp lệ).
     * @return ResponseEntity chứa UserResponse của người dùng đã đăng nhập.
     */
    @GetMapping("/in4")
    public ResponseEntity<UserResponse> getCurrentUser() {
        // Lấy ID người dùng từ phiên đăng nhập
        Long userId = userService.getCurrentUserId();
        UserResponse userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Endpoint cập nhật thông tin hồ sơ của người dùng hiện tại (fullname, password).
     * PATH: /api/auth/update
     * @param request Dữ liệu cập nhật từ client.
     * @return ResponseEntity chứa UserResponse sau khi đã được cập nhật.
     */
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateCurrentUser(@RequestBody UserUpdateRequest request) {
        Long userId = userService.getCurrentUserId();
        UserResponse updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Endpoint xóa tài khoản của người dùng hiện tại.
     * PATH: /api/auth/delete
     * * Sau khi xóa, người dùng sẽ không thể sử dụng token cũ nữa.
     * @return ResponseEntity chứa thông báo xác nhận việc xóa thành công.
     */
   @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteCurrentUser() {
        Long userId = userService.getCurrentUserId();
        userService.deleteUser(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Người dùng đã được xóa thành công");
        return ResponseEntity.ok(response);
    }


}
