package com.javaproject.Backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaproject.Backend.domain.Category;
/**
 * Interface Repository chịu trách nhiệm tương tác với cơ sở dữ liệu cho thực thể Danh mục (Category).
 * * Mở rộng JpaRepository để thừa hưởng các phương thức CRUD cơ bản.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Tìm kiếm ID của một Danh mục dựa trên Tên (name) và Loại (type) của nó.
     * * METHOD: JPQL Custom Query.
     * * Mục đích: Tối ưu hóa truy vấn bằng cách chỉ lấy trường ID (projection) thay vì tải toàn bộ đối tượng Category.
     * @param name Tên của Danh mục (ví dụ: "Ăn uống").
     * @param type Loại của Danh mục (ví dụ: "expense" hoặc "income").
     * @return Optional chứa ID của Category nếu tìm thấy, ngược lại là Optional.empty().
     * * * @Query: JPQL để chỉ định truy vấn tùy chỉnh.
     * * @Param: Liên kết tham số phương thức với tham số trong câu lệnh JPQL.
     */
    @Query("SELECT c.categoryId FROM Category c WHERE c.name = :name AND c.type = :type")
    Optional<Long> findIdByNameAndType(
            @Param("name") String name,
            @Param("type") String type);
}