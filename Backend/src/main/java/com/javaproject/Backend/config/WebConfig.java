package com.javaproject.Backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// ===== Cấu hình Web =====
// @Configuration: đánh dấu class này là cấu hình Spring, Spring sẽ tự động load
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ===== Cấu hình CORS (Cross-Origin Resource Sharing) =====
    // Cross-Origin Resource Sharing (CORS) là cơ chế ngăn chặn request từ các domain khác nếu không được phép.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  
        // Áp dụng CORS cho tất cả endpoint của API
                .allowedOrigins("*") 
                // Cho phép tất cả domain (trong production nên giới hạn domain cụ thể)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
                // Cho phép các phương thức HTTP mà client gửi để gọi API
                .allowedHeaders("*"); 
                // Cho phép tất cả header từ client
    }
}

//        FRONTEND (https://frontend.com)
//                      |
//      ┌───────────────▼───────────────┐
//      │      Browser gửi Request      │
//      │  GET /api/expenses (XHR/Fetch)│
//      │  Origin: https://frontend.com │
//      └───────────────┬───────────────┘
//                      |
//                      ▼
//           BACKEND (Spring Boot API)
//   ┌────────────────────────────────────┐
//   │ WebConfig.addCorsMappings()        │
//   │   - check allowedOrigins           │
//   │   - check allowedMethods           │
//   │   - check allowedHeaders           │
//   └────────────────────────────────────┘
//                      |
//       ┌──────────────▼──────────────┐
//       │Nếu request hợp lệ → tiếp tục│
//       │      đến Controller         │
//       └──────────────┬──────────────┘
//                      |
//                      ▼
//                 CONTROLLER
//       /api/expenses → trả JSON response
//                      |
//                      ▼
//                  BROWSER nhận
//             JSON data từ API

