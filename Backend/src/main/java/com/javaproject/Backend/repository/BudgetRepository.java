package com.javaproject.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaproject.Backend.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserUserId(Long userId);
    Optional<Budget> findByBudgetIdAndUserUserId(Long budgetId, Long userId);

    boolean existsByBudgetIdAndUserUserId(Long budgetId, Long userId);
}