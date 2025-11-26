package com.javaproject.Backend.dto.request;

// validation annotations của Jakarta Bean Validatio
// kiểm tra dữ liệu trước khi lưu vào DB hoặc xử lý request.
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class RegisterRequest {
    @Email // Kiểm tra dạng mail hợp lệ hay không
    @NotBlank // Kiểm tra chuỗi không null và không rỗng, đồng thời không chỉ chứa khoảng
              // trắng.
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
    private String checkpassword;
    private String fullName;
}