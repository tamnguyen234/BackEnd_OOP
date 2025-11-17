package com.javaproject.Backend.dto.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long categoryId;
    private Long userId;
    private String name;
    private String type; // expense / income
}
