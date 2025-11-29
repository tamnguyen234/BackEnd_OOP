package com.javaproject.Backend.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Đại diện cho thực thể Ngân sách (Budget) trong cơ sở dữ liệu.
 * * Lưu trữ giới hạn chi tiêu được thiết lập cho một Danh mục (Category) cụ thể,
 * trong một khoảng thời gian nhất định, bởi một Người dùng (User).
 * * Tương ứng với bảng "budgets" trong DB.
 */
@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {
    /**
     * Khóa chính của ngân sách
     * Tự động tăng (IDENTITY)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    /**
     * Mối quan hệ Many-to-One: Ngân sách thuộc về một Người dùng (User).
     * * fetch = FetchType.LAZY: Tải dữ liệu User khi cần thiết.
     * * @JoinColumn(name = "user_id"): Khóa ngoại trỏ đến bảng User (cột user_id).
     * * nullable = false: Đảm bảo Ngân sách luôn phải thuộc về một User.
     * * (Giả định: Thiết lập CASCADE DELETE ở cấp độ CSDL).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Mối quan hệ Many-to-One: Ngân sách được thiết lập cho một Danh mục (Category) chi tiêu.
     * * fetch = FetchType.LAZY: Tải dữ liệu Category khi cần thiết.
     * * @JoinColumn(name = "category_id"): Khóa ngoại trỏ đến bảng Category (cột category_id).
     * * nullable = false: Đảm bảo Ngân sách luôn liên kết với một Category.
     * * (Giả định: Thiết lập CASCADE DELETE ở cấp độ CSDL).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    /**
     * Giới hạn số tiền tối đa được chi tiêu trong 1 tháng
     * 12 chữ số, 3 chữ số sau dấu phẩy
     */
    @Column(name = "amount_limit", nullable = false, precision = 12, scale = 3)
    private BigDecimal amountLimit;
    /**
     * Thời gian bắt đầu mục tiêu 
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    /**
     * Thời gian kết thúc mục tiêu
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}