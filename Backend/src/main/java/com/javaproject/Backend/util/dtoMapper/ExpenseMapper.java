package com.javaproject.Backend.util.dtoMapper;

import com.javaproject.Backend.domain.Expense;
import com.javaproject.Backend.dto.response.ExpenseResponse;

public class ExpenseMapper {

    // Chuyển Expense entity sang ExpenseResponse DTO
    public static ExpenseResponse toDto(Expense e) {
        ExpenseResponse dto = new ExpenseResponse();   // Tạo object DTO

        dto.setExpenseId(e.getExpenseId());           // Lấy ID chi tiêu
        dto.setUserId(e.getUser().getUserId());       // Lấy ID user
        dto.setCategoryId(
            e.getCategory() != null ? e.getCategory().getCategoryId() : null
        );                                            // Lấy categoryId (có thể null)
        dto.setAmount(e.getAmount());                 // Lấy số tiền
        dto.setDescription(e.getDescription());       // Lấy mô tả
        dto.setExpenseDate(e.getExpenseDate());       // Lấy ngày chi tiêu
        dto.setCreatedAt(e.getCreatedAt());           // Lấy ngày tạo bản ghi

        return dto;                                   // Trả DTO
    }
}
