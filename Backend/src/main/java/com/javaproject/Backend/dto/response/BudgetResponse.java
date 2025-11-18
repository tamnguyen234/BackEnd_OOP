package com.javaproject.Backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetResponse {
    private Long budgetId;
    private Long userId;
    private Long categoryId;
    private BigDecimal amountLimit;
    private String period;
    private LocalDate startDate;
    private LocalDate endDate;
}