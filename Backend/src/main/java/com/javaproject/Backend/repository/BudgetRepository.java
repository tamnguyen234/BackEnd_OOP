package com.javaproject.Backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaproject.Backend.domain.Budget;

import jakarta.transaction.Transactional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserUserId(Long userId);
    Optional<Budget> findByBudgetIdAndUserUserId(Long budgetId, Long userId);

    boolean existsByBudgetIdAndUserUserId(Long budgetId, Long userId);
    // Trong BudgetRepository.java
    @Modifying
    @Transactional 
    @Query("DELETE FROM Budget b WHERE b.user.id = :userId AND b.endDate < :endDate")
    void deleteExpiredBudgetsByUserId(@Param("userId") Long userId, @Param("endDate") LocalDate endDate);
}