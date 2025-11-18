package com.javaproject.Backend.service.impl;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.response.RegisterResponse;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service thao tác trực tiếp với User entity
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // ==== Lấy thông tin một user theo userId ====
    @Override
    public RegisterResponse getUserById(Long userId) {
        // Tìm user theo ID, nếu không tồn tại thì ném exception
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        return response;
    }
    // ==== Lấy danh sách tất cả user ====
    public List<RegisterResponse> getAllUsers() {
        return userRepository.findAll()
                // Stream là một cách để xử lý tập hợp dữ liệu tuần tự hoặc song song mà không cần dùng vòng lặp for
                // gọi để tạo dữ liệu từ list -> thao tác lọc, biến đổi, sắp xếp
                .stream()
                // map hàm biến đổi, nhận từ stream chuyển thành RegisterResponse
                .map(user -> {
                    RegisterResponse dto = new RegisterResponse();
                    dto.setUserId(user.getUserId());
                    dto.setEmail(user.getEmail());
                    dto.setFullName(user.getFullName());
                    return dto;
                })
                // từ stream biến ngược về list bằng hàm collect trong collections
                .collect(Collectors.toList());
    }

    @Override
    public RegisterResponse updateUser(Long userId, RegisterResponse request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }
}

// ví dụ getallusers
// List<User>: [User1, User2, User3]
// |
// stream
// User1 → User2 → User3
// |
// map() từng dòng dữ liệu
// User1 → RegisterResponse1
// User2 → RegisterResponse2
// User3 → RegisterResponse3
// |
// list
// [RegisterResponse1, RegisterResponse2, RegisterResponse3]
