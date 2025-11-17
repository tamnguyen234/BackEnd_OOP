package com.javaproject.Backend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class BudgetRequest {
    private Long userId;
    private Long categoryId;
    private BigDecimal amountLimit;
    private String period; // MONTHLY, WEEKLY, ...
    private LocalDate startDate;
    private LocalDate endDate;
}
