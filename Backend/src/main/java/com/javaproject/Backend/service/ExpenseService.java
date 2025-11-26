package com.javaproject.Backend.service;

import java.time.LocalDate;
import java.util.List;

import com.javaproject.Backend.dto.request.ExpenseRequest;
import com.javaproject.Backend.dto.request.update.ExpenseUpdateRequest;
import com.javaproject.Backend.dto.response.ExpenseResponse;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest request);

    List<ExpenseResponse> getMyExpenses();
    List<ExpenseResponse> getExpensesByUser(Long userId);

    List<ExpenseResponse> getMyExpensesBetween(LocalDate start, LocalDate end);
    List<ExpenseResponse> getExpensesByUserBetween(Long userId, LocalDate start, LocalDate end);

    ExpenseResponse updateExpense(Long expenseId, ExpenseUpdateRequest request);

    void deleteExpense(Long expenseId);
}