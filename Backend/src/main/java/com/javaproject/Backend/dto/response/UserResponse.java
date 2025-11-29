package com.javaproject.Backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Data Transfer Object (DTO) dùng để chứa dữ liệu trả về thông tin người dùng từ BE sau khi đăng kí.
 * * Chỉ chứa các trường dữ liệu cần thiết để thay đổi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String fullName;
}
