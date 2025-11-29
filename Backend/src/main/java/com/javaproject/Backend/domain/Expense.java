package com.javaproject.Backend.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Đại diện cho thực thể Chi tiêu (expense) trong cơ sở dữ liệu.
 * * Lưu trữ chi tiết thông tin về chi tiêu và thu nhập đã diễn ra của người dùng 
 * * Tương ứng với bảng "expenses" trong DB.
 */
@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    /**
     * Khóa chính của expense
     * Tự động tăng (IDENTITY)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long expenseId;

    /**
     * Mối quan hệ Many-to-One: Chi tiêu thuộc về một Người dùng (User).
     * * fetch = FetchType.LAZY: Tải dữ liệu User khi cần thiết.
     * * @JoinColumn(name = "user_id"): Khóa ngoại trỏ đến bảng User (cột user_id).
     * * nullable = false: Đảm bảo Chi tiêu luôn phải thuộc về một User.
     * * (Giả định: Thiết lập CASCADE DELETE ở cấp độ CSDL).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Mối quan hệ Many-to-One: Có thể 1 hoặc nhiều loại giao dịch cho 1 category.
     * * @JoinColumn(name = "category_id"): Khóa ngoại trỏ đến bảng Category (cột category_id).
     * * nullable = false: Đảm bảo Chi tiêu luôn phải thuộc về một Cate nào đó.
     * * (Giả định: Thiết lập CASCADE DELETE ở cấp độ CSDL).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") 
    private Category category;
    /**
     * Số tiền dành cho giao dịch
     */
    @Column(name = "amount", nullable = false, precision = 12, scale = 3)
    private BigDecimal amount;
    /**
     * Mô tả chi tiết giao dịch
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    /** 
     * Ngày thực hiện giao dịch
     */
    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;
    /**
     * Ngày tạo giao dịch
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    /**
     * Chỉ thiết lập thời gian khi thực thể chưa được gán thời gian
     */
    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}