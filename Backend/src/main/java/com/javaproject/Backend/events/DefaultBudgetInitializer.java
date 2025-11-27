package com.javaproject.Backend.events;

import org.springframework.context.event.EventListener;

import com.javaproject.Backend.service.BudgetService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DefaultBudgetInitializer {
    private final BudgetService budgetService;

    @EventListener
    @Transactional
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        Long userId = event.getUserId();
        log.info("Khởi tạo budget mặc định cho User mới ID: {}", userId);
        
        budgetService.createMonthlyDefaultBudgets(userId); 
    }
}
