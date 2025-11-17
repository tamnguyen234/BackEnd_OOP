package com.javaproject.Backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.Expense;
import com.javaproject.Backend.domain.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Lấy tất cả chi tiêu của user
    List<Expense> findByUser(User user);

    // Lấy chi tiêu theo user và category
    List<Expense> findByUserAndCategory(User user, Category category);

    // Lấy chi tiêu trong khoảng thời gian
    List<Expense> findByUserAndExpenseDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
