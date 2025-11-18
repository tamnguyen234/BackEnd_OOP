package com.javaproject.Backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.javaproject.Backend.repository.BudgetRepository;

@Data
public class BudgetResponse {
    private Long budgetId;
    private Long userId;
    private Long categoryId;
    private BigDecimal amountLimit;
    private String period; // MONTHLY, WEEKLY
    private LocalDate startDate;
    private LocalDate endDate;

    public BudgetResponse() {
    };

    public BudgetResponse(Long budgetId, Long userId, Long categoryId,
            BigDecimal amountLimit, String period, LocalDate startDate, LocalDate endDate) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amountLimit = amountLimit;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
