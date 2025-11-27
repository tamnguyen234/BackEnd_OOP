package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.request.update.BudgetUpdateRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;

public interface BudgetService {
    Budget createBudget(Long userId ,BudgetRequest request);

    void createMonthlyDefaultBudgets(Long userId);

    List<BudgetResponse> getMyBudgets();

    BudgetResponse updateBudget(Long budgetId, BudgetUpdateRequest request);

    void deleteBudget(Long budgetId);

}
