package com.javaproject.Backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.Expense;
import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.ExpenseRequest;
import com.javaproject.Backend.dto.response.ExpenseResponse;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.CategoryRepository;
import com.javaproject.Backend.repository.ExpenseRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }
        Expense e = Expense.builder()
                .user(user)
                .category(category)
                .amount(request.getAmount())
                .description(request.getDescription())
                .expenseDate(request.getExpenseDate())
                .build();
        Expense saved = expenseRepository.save(e);
        return map(saved);
    }

    @Override
    public List<ExpenseResponse> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserUserId(userId).stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserBetween(Long userId, java.time.LocalDate start,
            java.time.LocalDate end) {
        return expenseRepository.findByUserUserIdAndExpenseDateBetween(userId, start, end)
                .stream().map(this::map).collect(Collectors.toList());
    }

    private ExpenseResponse map(Expense e) {
        return ExpenseResponse.builder()
                .expenseId(e.getExpenseId())
                .userId(e.getUser().getUserId())
                .categoryId(e.getCategory() != null ? e.getCategory().getCategoryId() : null)
                .amount(e.getAmount())
                .description(e.getDescription())
                .expenseDate(e.getExpenseDate())
                .build();
    }
}
