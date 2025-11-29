package com.javaproject.Backend.dto.request.update;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Data Transfer Object (DTO) dùng để chứa dữ liệu yêu cầu cập nhất Ngân sách (Budget).
 * * Chỉ chứa các trường dữ liệu cần thiết để thay đổi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetUpdateRequest {
    @DecimalMin("0.001")
    private BigDecimal amountLimit;
}