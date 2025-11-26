package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.request.update.BudgetUpdateRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;
import com.javaproject.Backend.dto.response.ExpenseResponse;

public interface BudgetService {
    BudgetResponse createBudget(BudgetRequest request);

    List<BudgetResponse> getMyBudgets();

    BudgetResponse updateBudget(Long budgetId, BudgetUpdateRequest request);

    void deleteBudget(Long budgetId);

}