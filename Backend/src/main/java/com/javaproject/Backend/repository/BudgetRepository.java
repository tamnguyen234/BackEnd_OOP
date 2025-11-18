package com.javaproject.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaproject.Backend.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserUserId(Long userId);
}