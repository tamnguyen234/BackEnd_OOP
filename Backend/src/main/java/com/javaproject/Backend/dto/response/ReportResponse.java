package com.javaproject.Backend.dto.response;

// File trả kết quả phân tích (AI/thuật toán tự viết).
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class ReportResponse {
    private BigDecimal totalExpense;
    private Map<String, BigDecimal> expenseByCategory;
    private String advice; 
}
