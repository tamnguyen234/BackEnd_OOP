package com.javaproject.Backend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) dùng để chứa dữ liệu yêu cầu từ client cho expense.
 * * Chỉ chứa các trường dữ liệu cần thiết để thay đổi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequest {

    @NotNull
    private String CategoryName;
    @NotNull
    private String CategoryType;

    @NotNull
    @DecimalMin("0.001")
    private BigDecimal amount;

    private String description;

    @NotNull
    private LocalDate expenseDate;
}