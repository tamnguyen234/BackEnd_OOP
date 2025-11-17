package com.javaproject.Backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaproject.Backend.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo email (ví dụ đăng nhập)
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
