package com.javaproject.Backend.util.dtoMapper;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.dto.response.BudgetResponse;

public class BudgetMapper {

    // Chuyển Budget entity sang BudgetResponse DTO
    public static BudgetResponse toDto(Budget b) {
        BudgetResponse dto = new BudgetResponse();     // Tạo object DTO

        dto.setBudgetId(b.getBudgetId());             // Lấy ID ngân sách
        dto.setUserId(b.getUser().getUserId());       // ID user
        dto.setCategoryId(b.getCategory().getCategoryId()); // ID category
        dto.setAmountLimit(b.getAmountLimit());       // Giới hạn số tiền
        dto.setPeriod(b.getPeriod());                 // Chu kỳ (MONTHLY / WEEKLY)
        dto.setStartDate(b.getStartDate());           // Ngày bắt đầu
        dto.setEndDate(b.getEndDate());               // Ngày kết thúc

        return dto;                                   // Trả DTO
    }
}

// Budget, Expense, Category → phức tạp, nhiều mối quan hệ → mapper giúp làm sạch dữ liệu trước khi trả API.
// User → đơn giản, chỉ cần info cơ bản → DTO đã đủ → mapper thừa.