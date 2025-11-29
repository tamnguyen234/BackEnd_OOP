package com.javaproject.Backend.dto.request.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) dùng để chứa dữ liệu yêu cầu cập nhật người dùng.
 * * Chỉ chứa các trường dữ liệu cần thiết để thay đổi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String oldPassword; 
    private String newPassword; 
    private String checkPassword; 
    private String fullName; 
}
