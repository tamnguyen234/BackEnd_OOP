package com.javaproject.Backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.CategoryRequest;
import com.javaproject.Backend.dto.request.update.CategoryUpdateRequest;
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
    @Override // Triển khai phương thức từ interface CategoryService
    public CategoryResponse createCategory(CategoryRequest request) {
        User user = userRepository.findById(userService.getCurrentUserId())
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
        Long currentUserId = userService.getCurrentUserId();

        // 4. Gọi phương thức truy vấn bằng userId đã được xác thực
        return getCategoriesByUser(currentUserId);
    }

    // ==== Truy xuất danh sách Category theo userId =====
    @Override
    public List<CategoryResponse> getCategoriesByUser(Long userId) {
        return categoryRepository.findByUserUserId(userId).stream().map(this::map).collect(Collectors.toList());
    }

    /** Cập nhật Category **/
    public CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest categoryUpdateRequest) {
        // 1. Lấy UserID hiện tại
        Long currentUserId = userService.getCurrentUserId();

        // 2. Tìm kiếm Category theo ID
        Category category = categoryRepository.findByCategoryIdAndUserUserId(categoryId, currentUserId)
                // Nếu không tìm thấy, ném ngoại lệ
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // 3. Kiểm tra quyền sở hữu (Security check)
        if (!category.getUser().getUserId().equals(currentUserId)) {
            // Ném ngoại lệ hoặc trả về lỗi nếu người dùng không sở hữu Category này
            throw new RuntimeException("Bạn không có quyền cập nhật Category này.");
        }

        // 4. Cập nhật các thuộc tính từ DTO
        if (StringUtils.hasText(categoryUpdateRequest.getName())) {
            category.setName(categoryUpdateRequest.getName());
        }

        // Cập nhật trường Type
        if (categoryUpdateRequest.getType() != null) {
            category.setType(categoryUpdateRequest.getType());
        }

        // 5. Lưu (Update) vào Database
        Category updatedCategory = categoryRepository.save(category);

        // 6. Ánh xạ (Map) sang Response DTO và trả về
        return map(updatedCategory); // Giả định bạn có hàm mapToCategoryResponse
    }

    /** Xóa Category **/
    public void deleteCategory(Long categoryId) {
        // 1. Lấy UserID hiện tại
        Long currentUserId = userService.getCurrentUserId();

        // 2. Tìm kiếm Category theo ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // 3. Kiểm tra quyền sở hữu (Security check)
        if (!category.getUser().getUserId().equals(currentUserId)) {
            throw new RuntimeException("Bạn không có quyền xóa Category này.");
        }

        // 4. Xóa khỏi Database
        categoryRepository.delete(category);
    }

    // ==== phương thức hỗ trợ chuyển đổi
    private CategoryResponse map(Category c) {
        return CategoryResponse.builder()
                .categoryId(c.getCategoryId())
                .name(c.getName())
                .type(c.getType())
                .build();
    }
}
