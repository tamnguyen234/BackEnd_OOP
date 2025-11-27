package com.javaproject.Backend.scheduler;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.repository.BudgetRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.BudgetService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyBudgetScheduler {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final BudgetService budgetService;
    /**
     * Lập lịch chạy vào 00:00:00 ngày 1 hàng tháng.
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void renewMonthlyBudgets() {
        log.info("Bắt đầu quy trình gia hạn ngân sách hàng tháng...");
        
        LocalDate newStartDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());

        List<User> users = userRepository.findAll(); 
        
        for (User user : users) {
            log.info("Đang xử lý ngân sách cho User ID: {}", user.getUserId());

            // 1. Xóa các ngân sách cũ của tháng trước
            // Phương thức này cần được thêm vào BudgetRepository
            budgetRepository.deleteBudgetsByUserUserIdAndEndDateLessThan(user.getUserId(), newStartDate); 
            
            // 2. GỌI PHƯƠNG THỨC CHUNG ĐÃ ĐƯỢC TỐI ƯU
            budgetService.createMonthlyDefaultBudgets(user.getUserId());
        }
        log.info("Kết thúc quy trình gia hạn ngân sách hàng tháng.");
    }
}
