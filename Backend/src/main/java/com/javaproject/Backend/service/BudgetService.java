package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;

public interface BudgetService {
    // ==== Tạo một ngân sách mới (Budget) ====
    BudgetResponse createBudget(BudgetRequest request);
    // ==== Truy xuất danh sách Ngân sách theo userId =====
    List<BudgetResponse> getBudgetsByUser(Long userId);

    List<BudgetResponse> getMyBudgets();
}