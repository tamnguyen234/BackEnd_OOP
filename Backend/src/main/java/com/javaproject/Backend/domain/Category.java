package com.javaproject.Backend.domain;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Đại diện cho thực thể Danh mục (Category) trong cơ sở dữ liệu.
 * * Danh mục được sử dụng để phân loại các giao dịch Chi tiêu (Expense) hoặc Ngân sách (Budget).
 * * Tương ứng với bảng "categories" trong DB.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    /**
     * ID duy nhất của Danh mục (Khóa chính).
     * * Tự động tăng (IDENTITY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    /**
     * Tên danh mục (mặc định 9 loại từ database)
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    /**
     * Loại giao dịch (chi tiêu và thu nhập)
     */
    @Column(name = "type", length = 10)
    private String type; 

    /**
     * Mối quan hệ One-to-Many với Chi tiêu (Expense).
     * * mappedBy = "category": Cho biết trường "category" trong class Expense chịu trách nhiệm
     * quản lý mối quan hệ và tạo Khóa ngoại (Foreign Key).
     * * cascade = CascadeType.PERSIST: Khi Category được lưu, các Expense liên quan cũng được lưu.
     * * Đây là trường collection giúp truy xuất danh sách Chi tiêu thuộc danh mục này.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private Set<Expense> expenses;

    /**
     * Mối quan hệ One-to-Many với Ngân sách (Budget).
     * * mappedBy = "category": Trường "category" trong class Budget quản lý mối quan hệ.
     * * cascade = CascadeType.REMOVE: Khi một Category bị xóa, tất cả các Budget liên quan
     * đến Category đó cũng sẽ bị xóa (tương đương với ON DELETE CASCADE ở cấp độ DB).
     * * Đây là trường collection giúp truy xuất danh sách Ngân sách thuộc danh mục này.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private Set<Budget> budgets;
}