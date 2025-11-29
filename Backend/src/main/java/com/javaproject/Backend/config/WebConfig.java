package com.javaproject.Backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình toàn bộ cho Spring Web MVC (WebConfig).
 * * Triển khai WebMvcConfigurer để tùy chỉnh các khía cạnh của cấu hình Web MVC,
 * chẳng hạn như CORS, định dạng, bộ chuyển đổi, v.v.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Cấu hình Cơ chế Chia sẻ Tài nguyên Lẫn miền (CORS - Cross-Origin Resource Sharing).
     * * Cho phép các ứng dụng khách (client) từ các miền khác có thể truy cập các API .
     * @param registry Đối tượng CorsRegistry để đăng ký cấu hình CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Áp dụng CORS cho tất cả endpoint của API
                .allowedOrigins("*")
                // Cho phép request từ các miền (*)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Cho phép các phương thức HTTP mà client gửi để gọi API
                .allowedHeaders("*");
                // Cho phép tất cả header từ client
    }
}

