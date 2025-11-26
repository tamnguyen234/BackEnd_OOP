package com.javaproject.Backend.service;

import com.javaproject.Backend.domain.Category;

public interface CategoryService {
    // Phương thức hỗ trợ quan trọng cho ExpenseService
    /**
     * Tìm kiếm Category theo tên và loại, và trả về đối tượng tham chiếu (Proxy)
     * để tối ưu hóa hiệu suất khi thiết lập khóa ngoại.
     * @param name Tên Category
     * @param type Loại Category (e.g., INCOME, EXPENSE)
     * @return Category Proxy
     * @throws com.javaproject.Backend.exception.ResourceNotFoundException nếu Category không tồn tại.
     */
    Category getReferenceByNameAndType(String name, String type);
}
