package com.javaproject.Backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.javaproject.Backend.service.ReportService;

import lombok.RequiredArgsConstructor;

// đánh dấu class này là Spring Bean, Spring sẽ tự động quản lý và khởi tạo.
@Component
@RequiredArgsConstructor
public class ReportScheduler {
    private final ReportService reportService;

    // Run at 03:00 on the 1st day of every month (cron)
    @Scheduled(cron = "0 0 3 1 * ?") // Đánh dấu phương thức thực hiện định kì
    public void runMonthly() {
        reportService.scheduledMonthlyReport();
        // gọi đến report service để tạo báo cáo hàng tháng.
    }
}
