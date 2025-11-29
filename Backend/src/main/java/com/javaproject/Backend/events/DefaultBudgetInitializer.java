package com.javaproject.Backend.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.javaproject.Backend.service.BudgetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * Component xử lý sự kiện (Event Listener) chịu trách nhiệm khởi tạo các Ngân sách
 * (Budget) mặc định cho một người dùng mới ngay sau khi tài khoản được tạo.
 * * Sử dụng @Slf4j để ghi nhật ký (logging).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultBudgetInitializer {
    private final BudgetService budgetService;

    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        Long userId = event.getUserId();
        // ghi log thông báo quá trình khởi tạo
        log.info("Khởi tạo budget mặc định cho User mới ID: {}", userId);
        budgetService.createMonthlyDefaultBudgets(userId); 
    }
}
