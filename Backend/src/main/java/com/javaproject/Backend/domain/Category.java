package com.javaproject.Backend.domain;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", length = 10)
    private String type; // Thường là 'expense' hoặc 'income'

    // Quan hệ OneToMany với Expense (cho phép Expense tham chiếu):
    // Không cần CascadeType.ALL ở đây, vì Expense có quy tắc SET NULL
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private Set<Expense> expenses;

    // Quan hệ OneToMany với Budget (cho phép Budget tham chiếu): ON DELETE CASCADE
    // Mặc dù Budget có ON DELETE CASCADE, ta không nên dùng CascadeType.ALL ở đây
    // vì Category là cha, nếu xóa Category, Budget sẽ bị xóa (theo DB)
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE) // REMOVE mô phỏng CASCADE trên JPA
    private Set<Budget> budgets;
}