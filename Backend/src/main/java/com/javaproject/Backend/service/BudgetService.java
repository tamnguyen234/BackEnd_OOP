package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;

public interface BudgetService {
    BudgetResponse createBudget(BudgetRequest request);

    List<BudgetResponse> getBudgetsByUser(Long userId);
}