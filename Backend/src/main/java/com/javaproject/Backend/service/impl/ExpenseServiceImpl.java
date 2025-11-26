package com.javaproject.Backend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.Expense;
import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.ExpenseRequest;
import com.javaproject.Backend.dto.request.update.ExpenseUpdateRequest;
import com.javaproject.Backend.dto.response.ExpenseResponse;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.CategoryRepository;
import com.javaproject.Backend.repository.ExpenseRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.ExpenseService;
import com.javaproject.Backend.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    // ====== Tạo Khoản Chi Mới =====
    @Override // Triển khai phương thức từ interface ExpenseService
    public ExpenseResponse createExpense(ExpenseRequest request) {
        User user = userRepository.findById(userService.getCurrentUserId())
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

    // ==== Logic truy cập dữ liệu cá nhân cho Expense ====
    @Override
    public List<ExpenseResponse> getMyExpenses() {
        Long currentUserId = userService.getCurrentUserId();

        // 3. Gọi phương thức truy vấn
        return getExpensesByUser(currentUserId);
    }

    // ==== Truy Vấn Tất Cả Chi Tiêu của Người Dùng =====
    @Override
    public List<ExpenseResponse> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserUserId(userId).stream().map(this::map).collect(Collectors.toList());
    }

    // ==== Logic truy cập dữ liệu cá nhân theo khoảng thời gian ====
    @Override
    public List<ExpenseResponse> getMyExpensesBetween(LocalDate start, LocalDate end) {
        Long currentUserId = userService.getCurrentUserId();

        // 3. Gọi phương thức truy vấn cũ (giờ đã an toàn vì userId được xác thực)
        return getExpensesByUserBetween(currentUserId, start, end);
    }

    // Phương thức truy vấn chung (triển khai dựa trên Repository)
    @Override
    public List<ExpenseResponse> getExpensesByUserBetween(Long userId, LocalDate start, LocalDate end) {
        // Phương thức này CẦN được triển khai trong ExpenseRepository
        return expenseRepository.findByUserUserIdAndExpenseDateBetween(userId, start, end)
                .stream().map(this::map).collect(Collectors.toList());
    }

    /** CẬP NHẬT Expense (PUT/PATCH) **/
    public ExpenseResponse updateExpense(Long expenseId, ExpenseUpdateRequest request) {
        Long currentUserId = userService.getCurrentUserId();

        // 1. Tìm kiếm và kiểm tra quyền sở hữu
        Expense expense = expenseRepository.findByExpenseIdAndUserUserId(expenseId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found or access denied."));

        // 2. Cập nhật các thuộc tính (Non-Null/Non-Blank Update)

        // Description (String - nên dùng hasText)
        if (StringUtils.hasText(request.getDescription())) {
            expense.setDescription(request.getDescription());
        }

        // Amount (Numeric - chỉ cần kiểm tra != null)
        if (request.getAmount() != null) {
            expense.setAmount(request.getAmount());
        }

        // Date (Date/Time - chỉ cần kiểm tra != null)
        if (request.getExpenseDate() != null) {
            expense.setExpenseDate(request.getExpenseDate());
        }

        // CategoryId (Khóa ngoại - Nếu thay đổi, cần tìm Category mới và set vào
        // Expense)
        if (request.getCategoryId() != null) {
            // Logic: Tìm Category theo ID mới. Đảm bảo Category mới thuộc về cùng UserID.
            Category newCategory = categoryRepository
                    .findByCategoryIdAndUserUserId(request.getCategoryId(), currentUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("New Category not found or access denied."));
            expense.setCategory(newCategory);
        }

        // 3. Lưu (Update) vào Database
        Expense updatedExpense = expenseRepository.save(expense);

        // 4. Ánh xạ (Map) sang Response DTO và trả về
        return map(updatedExpense);
    }

    /** XÓA Expense **/
    public void deleteExpense(Long expenseId) {
        Long currentUserId = userService.getCurrentUserId();

        // 1. Tìm kiếm và kiểm tra quyền sở hữu
        Expense expense = expenseRepository.findByExpenseIdAndUserUserId(expenseId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found or access denied."));

        // 2. Xóa khỏi Database
        expenseRepository.delete(expense);
    }

    // ==== map hỗ trợ chuyển đổi =====
    private ExpenseResponse map(Expense e) {
        return ExpenseResponse.builder()
                .expenseId(e.getExpenseId())
                .categoryId(e.getCategory() != null ? e.getCategory().getCategoryId() : null)
                .amount(e.getAmount())
                .description(e.getDescription())
                .expenseDate(e.getExpenseDate())
                .build();
    }
}
