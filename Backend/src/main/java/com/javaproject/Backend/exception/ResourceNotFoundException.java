package com.javaproject.Backend.exception;

// Lớp xử lý các tình huống khi một tài nguyên (ví dụ: User, Category, Budget) 
//không thể tìm thấy trong cơ sở dữ liệu, kế thừa từ lớp có sẵn
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message); // Được gọi trong các service khi không tìm thấy dữ liệu
    }
}

// RuntimeException thường được sử dụng cho các lỗi không thể phục hồi 
// (như lỗi lập trình hoặc lỗi cấu hình), nhưng trong các ứng dụng web hiện đại (như Spring Boot), 
// chúng thường được dùng cho các ngoại lệ nghiệp vụ như "không tìm thấy tài nguyên". 
// Lớp này sẽ được Spring Controller Advice (hoặc Global Exception Handler) bắt 
// và chuyển đổi thành một phản hồi HTTP phù hợp, thường là HTTP 404 Not Found.