package com.javaproject.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaproject.Backend.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Sử dụng @Query để chỉ trả về ID 
     */
    @Query("SELECT c.categoryId FROM Category c WHERE c.name = :name AND c.type = :type")
    Optional<Long> findIdByNameAndType(
        @Param("name") String name, 
        @Param("type") String type
    );
}