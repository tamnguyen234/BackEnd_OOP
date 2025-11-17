package com.javaproject.Backend.util.dtoMapper;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.dto.response.CategoryResponse;

public class CategoryMapper {

    // Chuyển Category entity sang CategoryResponse DTO
    public static CategoryResponse toDto(Category c) {
        CategoryResponse dto = new CategoryResponse();  // Tạo DTO rỗng

        dto.setCategoryId(c.getCategoryId());          // ID danh mục
        dto.setUserId(c.getUser().getUserId());        // ID user sở hữu danh mục
        dto.setName(c.getName());                       // Tên danh mục
        dto.setType(c.getType());                       // Loại danh mục (expense / income)

        return dto;                                    // Trả DTO cho controller
    }
}
