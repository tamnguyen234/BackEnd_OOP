package com.javaproject.Backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.CategoryRequest;
import com.javaproject.Backend.dto.response.CategoryResponse;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.CategoryRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.CategoryService;
import com.javaproject.Backend.service.UserService;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    // ==== Tạo category mới =====
    @Override //Triển khai phương thức từ interface CategoryService
    public CategoryResponse createCategory(CategoryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Category c = Category.builder()
                .user(user)
                .name(request.getName())
                .type(request.getType() == null ? "expense" : request.getType())
                .build();
        Category saved = categoryRepository.save(c);
        return map(saved);
    }
    // ⬇️⬇️⬇️ PHẦN BỔ SUNG LOGIC BẢO MẬT (Cách 1) ⬇️⬇️⬇️
    // ==== Truy xuất danh sách Category của người dùng đã đăng nhập =====
    public List<CategoryResponse> getMyCategories() {
        // 1. Lấy đối tượng Authentication (thông tin người dùng) từ Security Context Holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Lấy Principal (thường là email/username) từ Authentication Object
        // Đây chính là email bạn đã đặt trong JwtAuthenticationFilter
        String userEmail = authentication.getName(); 
        
        // 3. Tìm User Entity từ email
        User user = userService.findByEmail(userEmail)
                      .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        
        Long currentUserId = user.getUserId();
        
        // 4. Gọi phương thức truy vấn bằng userId đã được xác thực
        return getCategoriesByUser(currentUserId);
    }
    // ==== Truy xuất danh sách Category theo userId =====
    @Override
    public List<CategoryResponse> getCategoriesByUser(Long userId) {
        return categoryRepository.findByUserUserId(userId).stream().map(this::map).collect(Collectors.toList());
    }
    // ==== phương thức hỗ trợ chuyển đổi
    private CategoryResponse map(Category c) {
        return CategoryResponse.builder()
                .categoryId(c.getCategoryId())
                .userId(c.getUser().getUserId())
                .name(c.getName())
                .type(c.getType())
                .build();
    }
}
