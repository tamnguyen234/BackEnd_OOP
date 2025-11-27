package com.javaproject.Backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.scheduler.MonthlyBudgetScheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestSchedulerController {

    private final MonthlyBudgetScheduler scheduler;

    @PostMapping("/run-scheduler")
    public String runSchedulerManually() {
        log.info("========== CHẠY SCHEDULER THỦ CÔNG ==========");
        
        scheduler.renewMonthlyBudgets();

        log.info("========== SCHEDULER ĐÃ CHẠY XONG ==========");
        
        return "Scheduler executed manually!";
    }
}
