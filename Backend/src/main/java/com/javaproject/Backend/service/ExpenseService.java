package com.javaproject.Backend.service;

import java.time.LocalDate;
import java.util.List;

import com.javaproject.Backend.dto.request.ExpenseRequest;
import com.javaproject.Backend.dto.response.ExpenseResponse;

public interface ExpenseService {
    // ====== Tạo Khoản Chi Mới =====
    ExpenseResponse createExpense(ExpenseRequest request);
    // ==== Truy Vấn Tất Cả Chi Tiêu của Người Dùng =====
    List<ExpenseResponse> getExpensesByUser(Long userId);
    // // ==== Truy Vấn Chi Tiêu Theo Khoảng Thời Gian ======
    // ✅ PHƯƠNG THỨC MỚI: Lấy theo khoảng thời gian cho người dùng hiện tại
    List<ExpenseResponse> getMyExpensesBetween(LocalDate start, LocalDate end);
    List<ExpenseResponse> getExpensesByUserBetween(Long userId, LocalDate start, LocalDate end);
    List<ExpenseResponse> getMyExpenses();
}