package com.javaproject.Backend.domain;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Đại diện cho thực thể Người dùng (User) trong cơ sở dữ liệu.
 * * Lưu trữ thông tin cơ bản và các mối quan hệ với Chi tiêu (Expense) và Ngân sách (Budget).
 * * Tương ứng với bảng "users" trong DB.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /**
     * ID duy nhất của Người dùng (Khóa chính).
     * * Tự động tăng
     */
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    /**
     * Địa chỉ email của người dùng
     * unique = true: Đảm bảo email luôn là duy nhất
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Mật khẩu dưới dạng băm
     */
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;
    /**
     * Họ và tên của người dùng
     */
    @Column(name = "full_name", length = 100)
    private String fullName;
    /**
     * Ngày người dùng được tạo
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }

    /**
     * Mối quan hệ One-to-Many với Chi tiêu (Expense).
     * * mappedBy = "user": Trường "user" trong class Expense quản lý Khóa ngoại.
     * * cascade = CascadeType.ALL: Tất cả các thao tác (persist, merge, remove) sẽ được áp dụng cho Expense liên quan.
     * * orphanRemoval = true: Nếu một Expense bị gỡ khỏi Set này, nó sẽ bị xóa khỏi DB (tương đương ON DELETE CASCADE).
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Expense> expenses;

    /**
     * Mối quan hệ One-to-Many với Ngân sách (Budget).
     * * mappedBy = "user": Trường "user" trong class Budget quản lý Khóa ngoại.
     * * cascade = CascadeType.ALL: Tất cả các thao tác sẽ được áp dụng cho Budget liên quan.
     * * orphanRemoval = true: Nếu một Budget bị gỡ khỏi Set này, nó sẽ bị xóa khỏi DB (tương đương ON DELETE CASCADE).
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Budget> budgets;
}
