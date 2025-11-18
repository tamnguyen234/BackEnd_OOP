package com.javaproject.Backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// Đánh dấu class này là configuration class của Spring
@Configuration
// Bật cơ chế scheduling, cho phép các phương thức @Scheduled chạy tự động theo lịch
@EnableScheduling 
public class SchedulerConfig {
    // Để Spring biết rằng ứng dụng có sử dụng scheduling
}
