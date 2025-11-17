package com.javaproject.Backend.dto.request;

import lombok.Data;

// DTO dùng để nhận dữ liệu Đăng nhập (Authentication)
@Data 
//@Data = @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
// java tự sinh get, set, tostring, equals, hashcode, constructor rỗng
// Chỉ sử dụng không phải code dài
// Nếu muốn tạo constructor có tham số thì tự tạo
public class LoginRequest {
    private String email;
    private String password;
}