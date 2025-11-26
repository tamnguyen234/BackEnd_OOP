package com.javaproject.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaproject.Backend.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserUserId(Long userId);

    boolean existsByCategoryIdAndUserUserId(Long categoryId, Long userId);

    Optional<Category> findByCategoryIdAndUserUserId(Long categoryId, Long userId);
}