package com.javaproject.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.User;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Lấy tất cả category của một user
    List<Category> findByUser(User user);

    // Tìm theo user và type
    List<Category> findByUserAndType(User user, String type);
}
