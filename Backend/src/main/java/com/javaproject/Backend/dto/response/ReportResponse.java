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
/**
 * Data Transfer Object (DTO) dùng để chứa dữ liệu trả về thông tin báo cáo từ BE .
 * * Chỉ chứa các trường dữ liệu cần thiết để thay đổi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    @JsonSerialize(using = BigDecimalTrimZeroSerializer.class)
    private BigDecimal totalExpense;
}