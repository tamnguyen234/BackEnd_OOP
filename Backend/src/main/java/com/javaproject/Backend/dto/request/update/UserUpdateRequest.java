package com.javaproject.Backend.dto.request.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String oldPassword; // đổi password, password cũ bắt buộc phải điền
    private String newPassword; // có thể null nếu chỉ đổi fullname
    private String checkPassword; // dùng để xác nhận newPassword
    private String fullName; // có thể null nếu chỉ đổi password
}
