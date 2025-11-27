package com.javaproject.Backend.dto.response.ReportReponse;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ExpenseReportRow {
    private String categoryName;
    private BigDecimal amountSpent;
    private BigDecimal amountLimit;
    private BigDecimal difference;
}
