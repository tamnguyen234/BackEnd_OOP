package com.javaproject.Backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.CategoryRequest;
import com.javaproject.Backend.dto.response.CategoryResponse;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.CategoryRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
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

    @Override
    public List<CategoryResponse> getCategoriesByUser(Long userId) {
        return categoryRepository.findByUserUserId(userId).stream().map(this::map).collect(Collectors.toList());
    }

    private CategoryResponse map(Category c) {
        return CategoryResponse.builder()
                .categoryId(c.getCategoryId())
                .userId(c.getUser().getUserId())
                .name(c.getName())
                .type(c.getType())
                .build();
    }
}
