package com.javaproject.Backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.CategoryRequest;
import com.javaproject.Backend.dto.response.CategoryResponse;
import com.javaproject.Backend.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    // ==== Endpoint tạo Category ====
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest req) {
        return ResponseEntity.ok(categoryService.createCategory(req));
    }
    // ==== Endpoint truy xuất Category theo user ====
    // @GetMapping("/user/{userId}")
    // public ResponseEntity<List<CategoryResponse>> getByUser(@PathVariable Long userId) {
    //     return ResponseEntity.ok(categoryService.getCategoriesByUser(userId));
    // }
    // --- 2. Endpoint truy xuất Category theo người dùng đã đăng nhập ---
    // SỬA ĐỔI: Loại bỏ @PathVariable Long userId
    // Endpoint mới: GET /api/categories (Hoặc GET /api/categories/me)
    @GetMapping 
    public ResponseEntity<List<CategoryResponse>> getMyCategories() {
        // GỌI PHƯƠNG THỨC MỚI: Phương thức này sẽ lấy ID từ Security Context
        // và trả về Categories chỉ thuộc về người dùng đó.
        return ResponseEntity.ok(categoryService.getMyCategories());
    }
}
