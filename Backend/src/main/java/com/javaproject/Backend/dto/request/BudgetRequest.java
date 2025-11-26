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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRequest {
    // @NotNull
    // private Long userId;

    @NotNull
    private Long categoryId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amountLimit;

    @NotNull
    private String period;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}