package com.javaproject.Backend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
    // --- ExpenseRequest ---
@Data
public class ExpenseRequest {
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
}
