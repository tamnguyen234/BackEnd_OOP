package com.javaproject.Backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetResponse {
    private Long budgetId;
    private Long userId;
    private Long categoryId;
    private BigDecimal amountLimit;
    private String period;      // MONTHLY, WEEKLY
    private LocalDate startDate;
    private LocalDate endDate;
}
