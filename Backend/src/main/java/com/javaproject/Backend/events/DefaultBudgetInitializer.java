package com.javaproject.Backend.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.javaproject.Backend.service.BudgetService;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultBudgetInitializer {
    private final BudgetService budgetService;

    // @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        Long userId = event.getUserId();
        log.info("Khởi tạo budget mặc định cho User mới ID: {}", userId);
        budgetService.createMonthlyDefaultBudgets(userId); 
    }
}
