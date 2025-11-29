package com.javaproject.Backend.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.update.UserUpdateRequest;
import com.javaproject.Backend.dto.response.UserResponse;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.UserService;

import lombok.RequiredArgsConstructor;
/**
 * Triển khai (Implementation) của UserService, xử lý logic nghiệp vụ cho Quản lý Người dùng (User Management).
 * * @Service: Đánh dấu class này là Service Component của Spring.
 * * @RequiredArgsConstructor: Tự động tạo constructor với các trường final (Dependency Injection).
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lấy danh sách ID của tất cả người dùng trong hệ thống.
     */
    @Override
    public List<Long> getAllUserIds() {
        return userRepository.findAll().stream().map(User::getUserId).collect(Collectors.toList());
    }

    /**
     * Lấy ID của người dùng hiện tại đang gửi request, trích xuất từ Spring Security Context.
     * * Phương thức này là cốt lõi cho việc đảm bảo quyền truy cập (Security).
     * @return ID của người dùng (Long).
     * @throws AccessDeniedException nếu không tìm thấy ID trong Security Context.
     */
    @Override
    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        throw new AccessDeniedException("User ID not found or not authenticated.");
    }

    /**
     * Truy xuất thông tin người dùng theo ID và chuyển đổi sang DTO an toàn.
     * @param userId ID của người dùng cần tìm.
     * @return UserResponse DTO.
     * @throws RuntimeException nếu User không tồn tại.
     */
    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    /**
     * Cập nhật thông tin hồ sơ (fullName) và/hoặc mật khẩu của người dùng.
     * @throws RuntimeException nếu User không tồn tại.
     * @throws IllegalArgumentException nếu mật khẩu cũ sai hoặc mật khẩu mới không khớp xác nhận.
     */
    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cập nhật password nếu có
        if (request.getNewPassword() != null) {
            if (request.getOldPassword() == null ||
                    !passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            if (!request.getNewPassword().equals(request.getCheckPassword())) {
                throw new IllegalArgumentException("New password and confirmation do not match");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        }

        // Cập nhật fullName nếu có
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }

        // Lưu user
        userRepository.save(user);

        return UserResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    /**
     * Xóa tài khoản người dùng khỏi hệ thống.
     * @throws RuntimeException nếu User không tồn tại.
     */
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    /**
     * Tìm kiếm User Entity theo Email.
     */
    @Override
    public Optional<User> findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }
    /**
     * Phương thức cập nhật bị bỏ qua (Unimplemented/Deprecated).
     * * Phương thức này có chữ ký tương tự nhưng sử dụng UserResponse làm request DTO,
     * thường không được khuyến nghị cho thao tác cập nhật (nên dùng DTO riêng biệt như UserUpdateRequest).
     */
    @Override
    public UserResponse updateUser(Long userId, UserResponse request) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }
}
