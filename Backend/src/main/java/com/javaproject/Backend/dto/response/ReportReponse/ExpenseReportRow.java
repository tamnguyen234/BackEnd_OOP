package com.javaproject.Backend.dto.response.ReportReponse;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaproject.Backend.util.BigDecimalTrimZeroSerializer;

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
    @JsonSerialize(using = BigDecimalTrimZeroSerializer.class)
    private BigDecimal amountSpent;
    @JsonSerialize(using = BigDecimalTrimZeroSerializer.class)
    private BigDecimal amountLimit;
    @JsonSerialize(using = BigDecimalTrimZeroSerializer.class)
    private BigDecimal difference;
}
