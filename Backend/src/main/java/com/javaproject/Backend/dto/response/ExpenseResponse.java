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
 * Data Transfer Object (DTO) dùng để chứa dữ liệu trả về thông tin chi tiêu từ BE .
 * * Chỉ chứa các trường dữ liệu cần thiết để thay đổi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {
    private Long expenseId;
    private String CategoryName;
    private String CategoryType;
    @JsonSerialize(using = BigDecimalTrimZeroSerializer.class)
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
}