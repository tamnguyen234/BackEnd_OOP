package com.javaproject.Backend.exception;
/**
 * Lớp xử lý các tình huống khi một tài nguyên (ví dụ: User, Category, Budget) 
 * không thể tìm thấy trong cơ sở dữ liệu, kế thừa từ lớp có sẵn
 */
// 
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message); 
    }
}