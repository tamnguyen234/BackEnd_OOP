package com.javaproject.Backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaproject.Backend.domain.ExpenseReportArchive;

import jakarta.transaction.Transactional;

/**
 * Interface Repository chịu trách nhiệm tương tác với cơ sở dữ liệu cho thực thể
 * Lưu trữ Báo cáo Chi tiêu (ExpenseReportArchive).
 * * Mở rộng JpaRepository để thừa hưởng các phương thức CRUD cơ bản.
 * * Được sử dụng để lưu trữ và truy xuất các báo cáo đã được tạo để tăng tốc độ truy vấn.
 */
public interface ExpenseReportArchiveRepository
        extends JpaRepository<ExpenseReportArchive, Long> {

    /**
     * Tìm và trả về bản lưu trữ báo cáo chi tiêu dựa trên ID người dùng, Tháng, và Năm.
     * * METHOD: Derived Query.
     * * Dùng để kiểm tra xem báo cáo đã được tạo và lưu trữ cho tháng/năm đó chưa.
     *
     * @param userId ID của người dùng sở hữu báo cáo.
     * @param month Tháng của báo cáo.
     * @param year Năm của báo cáo.
     * @return Optional chứa ExpenseReportArchive nếu tìm thấy, ngược lại là Optional.empty().
     */
    Optional<ExpenseReportArchive> findByUserIdAndMonthAndYear(
            Long userId, int month, int year);

    /**
     * Xóa tất cả các bản lưu trữ báo cáo cũ hơn một tháng/năm tối thiểu được chỉ định.
     * * Thường được sử dụng trong các tác vụ dọn dẹp theo lịch (scheduling) để loại bỏ
     * các báo cáo quá cũ khỏi cache.
     *
     * @param userId ID của người dùng sở hữu các báo cáo cần dọn dẹp.
     * @param minMonth Tháng tối thiểu (báo cáo có tháng nhỏ hơn sẽ bị xóa).
     * @param minYear Năm tối thiểu (báo cáo có năm nhỏ hơn sẽ bị xóa).
     *
     * * @Modifying: Cho biết đây là truy vấn sửa đổi dữ liệu (DELETE).
     * * @Transactional: Đảm bảo thao tác này được thực hiện trong một Transaction.
     * * @Query: JPQL phức tạp để kiểm tra cả năm và tháng.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ExpenseReportArchive e " +
            "WHERE e.userId = :userId " +
            "AND (e.year < :minYear OR (e.year = :minYear AND e.month < :minMonth))")
    void deleteOlderThan(
            @Param("userId") Long userId,
            @Param("minMonth") int minMonth,
            @Param("minYear") int minYear);

}
