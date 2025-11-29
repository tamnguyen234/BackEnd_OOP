package com.javaproject.Backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaproject.Backend.domain.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserUserId(Long userId);

    List<Expense> findByUserUserIdAndExpenseDateBetween(Long userId, LocalDate start, LocalDate end);

    Optional<Expense> findByExpenseIdAndUserUserId(Long expenseId, Long userId);

    boolean existsByExpenseIdAndUserUserId(Long expenseId, Long userId);

    List<Expense> findByExpenseDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT e.category.name, SUM(e.amount) FROM Expense e WHERE e.user.userId = :userId GROUP BY e.category.name")
    List<Object[]> getExpenseReportByUser(@Param("userId") Long userId);

    // PHƯƠNG THỨC TÍNH TỔNG
    @Query("SELECT COALESCE(SUM(e.amount), 0) " +
            "FROM Expense e " +
            "WHERE e.user.userId = :userId " +
            "AND e.category.categoryId = :categoryId " +
            "AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalExpense(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}