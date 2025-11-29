package com.javaproject.Backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Cấu hình cho việc lập lịch (Scheduling)
 * * Đánh dấu class là một Configuration class để Spring container biết và xử lý.
 * Kích hoạt khả năng lập lịch cho phương thức được đánh dấu @Scheduled
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // Để Spring biết rằng ứng dụng có sử dụng scheduling
}
