package com.javaproject.Backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaproject.Backend.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserUserId(Long userId);

    Optional<Budget> findByBudgetIdAndUserUserId(Long budgetId, Long userId);

    // Lấy tất cả budget của user mà trong khoảng startDate-endDate truyền vào
    List<Budget> findByUserUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long userId, LocalDate endDate, LocalDate startDate);

    boolean existsByBudgetIdAndUserUserId(Long budgetId, Long userId);

    void deleteBudgetsByUserUserIdAndEndDateLessThan(Long userId, LocalDate newStartDate);
}