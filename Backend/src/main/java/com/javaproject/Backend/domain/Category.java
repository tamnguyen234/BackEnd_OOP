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

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", length = 10)
    private String type; // Thường là 'expense' hoặc 'income'

    // Quan hệ OneToMany với Expense (cho phép Expense tham chiếu):
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private Set<Expense> expenses;

    // Quan hệ OneToMany với Budget (cho phép Budget tham chiếu): ON DELETE CASCADE
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE) 
    private Set<Budget> budgets;
}