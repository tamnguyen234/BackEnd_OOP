package com.javaproject.Backend.dto.response.ReportReponse;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseReportRow {
    private String categoryName;
    private BigDecimal amountSpent; // tổng chi
    private BigDecimal amountLimit; // giới hạn
    private BigDecimal difference; // số tiền còn lại so với limit
}
