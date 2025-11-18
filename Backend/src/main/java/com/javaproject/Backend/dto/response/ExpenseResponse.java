package com.javaproject.Backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO dùng để trả về thông tin chi tiết của một giao dịch Chi tiêu (Expense)
 * cho người dùng cuối.
 */
import lombok.Data;

@Data
public class ExpenseResponse {
    private Long expenseId;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;

    public ExpenseResponse() {
    };

    public ExpenseResponse(Long expenseId, Long userId, Long categoryId,
            BigDecimal amount, String description, LocalDate expenseDate, LocalDateTime createdAt) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.createdAt = createdAt;
    }
}