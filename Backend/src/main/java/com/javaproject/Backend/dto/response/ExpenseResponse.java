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
public class ExpenseResponse {
    private Long expenseId;
    private String CategoryName;
    private String CategoryType;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
}