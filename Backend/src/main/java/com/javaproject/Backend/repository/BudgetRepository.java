package com.javaproject.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.User;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Lấy tất cả ngân sách của user
    List<Budget> findByUser(User user);

    // Lấy ngân sách theo user và category
    List<Budget> findByUserAndCategory(User user, Category category);
}
