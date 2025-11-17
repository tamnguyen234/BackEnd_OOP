package com.javaproject.Backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO dùng để trả về thông tin chi tiết của một giao dịch Chi tiêu (Expense)
 * cho người dùng cuối.
 */
public class ExpenseResponse {
    private Long expenseId;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;
    private String categoryName; // Tên danh mục (thường được join từ bảng categories)

    // Constructors
    public ExpenseResponse() {
    }

    // ----------------------------------------------------------------------
    // GETTERS VÀ SETTERS ĐẦY ĐỦ
    // ----------------------------------------------------------------------

    // expenseId
    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    // userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // categoryId
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // amount
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // expenseDate
    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    // createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // categoryName
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}