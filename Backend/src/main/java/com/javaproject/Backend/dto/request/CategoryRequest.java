package com.javaproject.Backend.dto.request;

import lombok.Data;

// --- CategoryRequest ---
@Data
public class CategoryRequest {
    private Long userId;
    private String name;
    private String type; // income / expense
}
