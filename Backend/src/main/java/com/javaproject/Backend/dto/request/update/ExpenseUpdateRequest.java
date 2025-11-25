package com.javaproject.Backend.dto.request.update;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class ExpenseUpdateRequest {
    // @NotNull
    // private Long userId;

    private Long categoryId;

    @DecimalMin("0.01")
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
}