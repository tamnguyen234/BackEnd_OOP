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
 * Data Transfer Object (DTO) dùng để chứa dữ liệu yêu cầu từ client cho budget.
 * * Chỉ chứa các trường dữ liệu cần thiết để thay đổi.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequest {
    
    @NotNull
    private String CategoryName;
    @NotNull
    @DecimalMin("0.001")
    private BigDecimal amountLimit;

    private LocalDate startDate;

    private LocalDate endDate;
}