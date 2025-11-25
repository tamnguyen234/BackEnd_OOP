package com.javaproject.Backend.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id // đánh dấu primarykey
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    // Tự động sinh giá trị primary key (ID) cho entity khi lưu vào database.
    private Long userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    // Đánh dấu createdAT sẽ được gán tự động
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
    // Quan hệ OneToMany với Category: ON DELETE CASCADE
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> categories;

    // Quan hệ OneToMany với Expense: ON DELETE CASCADE
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Expense> expenses;

    // Quan hệ OneToMany với Budget: ON DELETE CASCADE
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Budget> budgets;
}
