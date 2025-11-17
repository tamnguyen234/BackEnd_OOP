package com.javaproject.Backend.dto.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long categoryId;
    private Long userId;
    private String name;
    private String type; // expense / income

    public CategoryResponse(Long categoryId, Long userId, String name, String type){
        this.categoryId = categoryId;
        this.userId = userId;
        this.name = name;
        this.type = type;
    }
}
