package com.javaproject.Backend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO dùng để tạo hoặc cập nhật một giao dịch Chi tiêu (Expense)
public class ExpenseRequest {
    private Long categoryId; // ID danh mục chi tiêu
    private BigDecimal amount; // Số tiền chi tiêu (DECIMAL(12, 2))
    private String description; // Mô tả giao dịch (TEXT)
    private LocalDate expenseDate; // Ngày phát sinh chi tiêu (DATE NOT NULL)

    // Constructors
    public ExpenseRequest() {
    }

    // Getters và Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
}