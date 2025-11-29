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

/**
 * Cấu hình bảo mật chính (Security Configuration) cho ứng dụng Spring Boot.
 * * Sử dụng JWT (JSON Web Token) để xác thực người dùng (Stateless Authentication).
 * * Kích hoạt bảo mật cấp phương thức (@EnableMethodSecurity) để kiểm tra quyền truy cập
 * ở cấp độ @PreAuthorize và @PostAuthorize.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    // JwtUtils dùng cho filter xác thực token
    private final JwtUtils jwtUtils;
    
    // ===== Cấu hình Bean =====

    /**
     * BCryptPasswordEncoder: mã hóa mật khẩu an toàn
     * @return Đối tượng PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cung cấp Bean JwtAuthenticationFilter
     * Filter kiểm tra JWT token gửi từ client
     * @return Đối tượng Filter
     */
    @Bean
    public Filter jwtAuthenticationFilter() {
        return new com.javaproject.Backend.util.JwtAuthenticationFilter(jwtUtils);
    }

    // ===== Cấu hình HTTP Security =====

    /**
     * Định nghĩa chuỗi bộ lọc bảo mật (SecurityFilterChain) cho các HTTP requests.
     * * Cấu hình các quy tắc bảo mật, quản lý session, và thứ tự các bộ lọc.
     * @param http Đối tượng HttpSecurity để tùy chỉnh cấu hình bảo mật.
     * @return SecurityFilterChain đã được xây dựng.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                // Tắt CSRF, vì API dùng token để duy trì đăng nhập 
                .authorizeHttpRequests(auth -> auth
                    // permitall - Cho phép truy cập cho các đường dẫn
                        // * /api/auth/**: Các endpoint liên quan đến Đăng ký/Đăng nhập (authentication).
                        // * /h2-console/**: H2 Database console .
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                        // Yêu cầu xác thực với request khác
                        .anyRequest().authenticated()) 
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Đảm báo kiểm tra token
        http.addFilterBefore(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);

        // Cấu hình H2 console: để H2 console hoạt động khi được nhúng trong iframe
        http.headers(headers -> headers.frameOptions().disable());
        return http.build();
    }
}