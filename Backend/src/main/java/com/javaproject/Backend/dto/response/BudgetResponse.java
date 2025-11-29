package com.javaproject.Backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaproject.Backend.util.BigDecimalTrimZeroSerializer;

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
    private String CategoryName;
    @JsonSerialize(using = BigDecimalTrimZeroSerializer.class)
    private BigDecimal amountLimit;
    @JsonSerialize(using = BigDecimalTrimZeroSerializer.class)
    private BigDecimal totalExpense;
    private LocalDate startDate;
    private LocalDate endDate;
}