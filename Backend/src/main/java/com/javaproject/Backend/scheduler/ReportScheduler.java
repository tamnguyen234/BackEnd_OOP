package com.javaproject.Backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.javaproject.Backend.service.ReportService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportScheduler {
    private final ReportService reportService;

    // Run at 03:00 on the 1st day of every month (cron)
    @Scheduled(cron = "0 0 3 1 * ?")
    public void runMonthly() {
        reportService.scheduledMonthlyReport();
    }
}
