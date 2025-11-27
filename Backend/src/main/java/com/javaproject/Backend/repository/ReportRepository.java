package com.javaproject.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.domain.Expense;

public class ReportRepository {
    @Repository
    public interface ExpenseRepository extends JpaRepository<Expense, Long> {
        List<Expense> findByUserUserId(Long userId);
    }

    public interface BudgetRepository extends JpaRepository<Budget, Long> {
    }

}
