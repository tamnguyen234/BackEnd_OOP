package com.javaproject.Backend.dto.response;

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
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    // private Long userId;
    private String email;
}
// jwt = json web token Là một chuỗi ký tự dùng để xác thực và truyền thông tin giữa client và server.