package com.javaproject.Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.javaproject.Backend.util.JwtUtils;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtUtils jwtUtils;
    // JwtUtils dùng cho filter xác thực token

    // ===== Bean cho PasswordEncoder =====
    @Bean
    public PasswordEncoder passwordEncoder() {
         // BCryptPasswordEncoder: mã hóa mật khẩu an toàn
        return new BCryptPasswordEncoder();
        // dùng passwordEncoder.matches(rawPassword, encodedPassword) để kiểm tra mật khẩu.
    }

     // ===== Bean cho JWT Filter =====
    @Bean
    public Filter jwtAuthenticationFilter() {
        // Tạo filter dùng để kiểm tra JWT token gửi từ client trong header Authorization
        return new com.javaproject.Backend.util.JwtAuthenticationFilter(jwtUtils);
        // hợp lệ trả filter đến JwtAuthenticationFilter (trong util)
    }

     // ===== Cấu hình bảo mật tổng thể cho HTTP requests =====
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) 
        // Tắt CSRF, vì API thường dùng token (không dùng form) - dễ hiểu là tắt cảnh báo không liên quan
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll() 
                        // permitall - cho phép tất cả
                        // Các đường dẫn đăng ký, login và H2 console được phép truy cập mà không cần auth
                        .anyRequest().authenticated()) // request khác cần xác thực
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                // Sau mỗi lần kiểm tra jwt token xong và trả kết quả thì không nhớ nó nữa
        
            // Thêm filter JWT trước filter mặc định UsernamePasswordAuthenticationFilter
                http.addFilterBefore(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);

        // Cho phép truy cập H2 console nếu đang dùng, tắt frame options
        http.headers(headers -> headers.frameOptions().disable());
        return http.build();
        // Build SecurityFilterChain và trả về cho Spring Security sử dụng
    }
}
// CLIENT (browser/app)
//         |
//         | POST /api/auth/login {email, password}
//         v
// CONTROLLER (AuthController)
//         |
//         | gọi AuthService.authenticate()
//         v
// SERVICE (AuthServiceImpl)
//         |
//         | 1. Lấy user từ DB
//         | 2. So sánh password raw + hashed
//         | 3. Tạo JWT token (jwtUtils.generateToken)
//         v
// RESPONSE
//         |
//         | Trả về JSON:
//         | { token: "xxx.yyy.zzz", userId: 1, email: "abc@gmail.com" }
//         v
// CLIENT
//         |
//         | Lưu token (localStorage hoặc memory)
//         | Mỗi request sau kèm header:
//         | Authorization: Bearer xxx.yyy.zzz
//         v
// SPRING SECURITY FILTER (jwtAuthenticationFilter)
//         |
//         | Kiểm tra token JWT
//         | Nếu hợp lệ → gắn thông tin user vào SecurityContext
//         v
// CONTROLLER (requested endpoint)
//         |
//         | Controller biết request thuộc user nào
//         | Xử lý nghiệp vụ
//         v
// RESPONSE
