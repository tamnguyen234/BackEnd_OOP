package com.javaproject.Backend.dto.request.update;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale.Category;

import jakarta.validation.constraints.DecimalMin;
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
public class BudgetUpdateRequest {
    private Long categoryId;
    @DecimalMin("0.01")
    private String CategoryName;
    private BigDecimal amountLimit;
    private LocalDate startDate;
    private LocalDate endDate;
}