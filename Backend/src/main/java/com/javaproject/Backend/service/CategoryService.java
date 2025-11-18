package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.request.CategoryRequest;
import com.javaproject.Backend.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getCategoriesByUser(Long userId);
}