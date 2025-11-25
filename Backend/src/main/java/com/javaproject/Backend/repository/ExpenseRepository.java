package com.javaproject.Backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaproject.Backend.domain.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserUserId(Long userId);

    List<Expense> findByUserUserIdAndExpenseDateBetween(Long userId, LocalDate start, LocalDate end);

    List<Expense> findByExpenseDateBetween(LocalDate start, LocalDate end);
    
    // "Chỉ tìm Expense với ID X nếu nó thuộc về User Y đang gửi request."
    // phương thức quan trọng nhất để triển khai nguyên tắc bảo mật User's Own Data (Dữ liệu của chính người dùng) 
    // trong các thao tác Cập nhật (Update) và Xóa (Delete).
    Optional<Expense> findByExpenseIdAndUserUserId(Long expenseId, Long userId);
    
    // bảo mật cho delete
    boolean existsByExpenseIdAndUserUserId(Long expenseId, Long userId);

}