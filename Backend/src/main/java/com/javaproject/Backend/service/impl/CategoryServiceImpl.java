package com.javaproject.Backend.service.impl;

import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.CategoryRepository;
import com.javaproject.Backend.service.CategoryService;

import lombok.RequiredArgsConstructor;
/**
 * Triển khai (Implementation) của CategoryService, xử lý logic nghiệp vụ cho Danh mục (Category).
 * * @Service: Đánh dấu class này là Service Component của Spring.
 * * @RequiredArgsConstructor: Tự động tạo constructor với các trường final (Dependency Injection).
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    // --- Phương thức hỗ trợ cho các Service khác ---
    public Category getReferenceByNameAndType(String name, String type) {
        // 1. Tìm kiếm Category ID
        Long categoryId = categoryRepository
                .findIdByNameAndType(name, type)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with Name: " + name + " and Type: " + type
                ));

        // 2. Trả về đối tượng tham chiếu (Proxy Category)
        return categoryRepository.getReferenceById(categoryId);
    }
}
