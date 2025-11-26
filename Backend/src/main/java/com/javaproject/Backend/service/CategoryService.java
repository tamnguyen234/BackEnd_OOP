package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.request.CategoryRequest;
import com.javaproject.Backend.dto.request.update.CategoryUpdateRequest;
import com.javaproject.Backend.dto.response.CategoryResponse;

public interface CategoryService {
    // ==== Tạo category mới =====
    CategoryResponse createCategory(CategoryRequest request);

    // ==== Truy xuất danh sách Category theo userId =====
    List<CategoryResponse> getCategoriesByUser(Long userId);

    List<CategoryResponse> getMyCategories();

    CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest request);

    void deleteCategory(Long categoryId);
}