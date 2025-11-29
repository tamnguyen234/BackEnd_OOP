package com.javaproject.Backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaproject.Backend.domain.Expense;
/**
 * Interface Repository chịu trách nhiệm tương tác với cơ sở dữ liệu cho thực thể Chi tiêu (Expense).
 * * Mở rộng JpaRepository để thừa hưởng các phương thức CRUD cơ bản.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
        /**
         * Tìm và trả về tất cả các giao dịch Chi tiêu thuộc về một người dùng cụ thể.
         * @param userId ID của người dùng.
         * @return Danh sách các Expense.
         */
        List<Expense> findByUserUserId(Long userId);

       /**
         * Tìm các giao dịch Chi tiêu của một người dùng trong một khoảng thời gian cụ thể.
         * @param userId ID của người dùng.
         * @param start Ngày bắt đầu (bao gồm).
         * @param end Ngày kết thúc (bao gồm).
         * @return Danh sách các Expense trong khoảng thời gian đó.
         */
        List<Expense> findByUserUserIdAndExpenseDateBetween(Long userId, LocalDate start, LocalDate end);

        /**
         * Tìm một giao dịch Chi tiêu theo ID của nó, đảm bảo rằng nó thuộc về người dùng có userId tương ứng.
         * * Dùng để kiểm tra quyền sở hữu trước khi thực hiện các thao tác Sửa/Xóa.
         * @param expenseId ID của giao dịch Chi tiêu.
         * @param userId ID của người dùng.
         * @return Optional chứa Expense nếu tìm thấy và thuộc về user.
         */
        Optional<Expense> findByExpenseIdAndUserUserId(Long expenseId, Long userId);

        /**
         * Kiểm tra xem một giao dịch Chi tiêu có tồn tại hay không, và phải thuộc về người dùng cụ thể.
         * * Tối ưu hơn cho các kiểm tra điều kiện so với việc tải toàn bộ đối tượng.
         * @param expenseId ID của giao dịch Chi tiêu.
         * @param userId ID của người dùng.
         * @return true nếu tồn tại, ngược lại là false.
         */
        boolean existsByExpenseIdAndUserUserId(Long expenseId, Long userId);

        /**
         * Tìm tất cả các giao dịch Chi tiêu trong một khoảng thời gian cụ thể (Không phân biệt User).
         * * Phương thức này ít dùng trong ứng dụng thực tế (vì thiếu userId) nhưng hợp lệ về cú pháp.
         * @param start Ngày bắt đầu (bao gồm).
         * @param end Ngày kết thúc (bao gồm).
         * @return Danh sách các Expense.
         */
        List<Expense> findByExpenseDateBetween(LocalDate start, LocalDate end);

        /**
         * Tạo báo cáo chi tiêu bằng cách nhóm theo Danh mục và tính tổng số tiền.
         * * METHOD: JPQL Group By Query.
         * * Chỉ áp dụng cho chi tiêu của người dùng được chỉ định.
         * @param userId ID của người dùng.
         * @return List<Object[]>: Mỗi phần tử là một mảng Object chứa [Tên danh mục, Tổng số tiền].
         */
        @Query("SELECT e.category.name, SUM(e.amount) FROM Expense e WHERE e.user.userId = :userId GROUP BY e.category.name")
        List<Object[]> getExpenseReportByUser(@Param("userId") Long userId);

        /**
         * Tính tổng số tiền chi tiêu cho một Danh mục cụ thể của một Người dùng trong một khoảng thời gian.
         * * METHOD: JPQL Aggregate Query (SUM).
         * * Được dùng để kiểm tra giới hạn Ngân sách (Budget).
         *
         * @param userId ID của người dùng.
         * @param categoryId ID của Danh mục.
         * @param startDate Ngày bắt đầu.
         * @param endDate Ngày kết thúc.
         * @return Tổng số tiền (BigDecimal) đã chi tiêu. Trả về 0 nếu không có giao dịch (nhờ COALESCE).
         */
        @Query("SELECT COALESCE(SUM(e.amount), 0) " + // COALESCE(SUM, 0) đảm bảo trả về 0 thay vì null
                "FROM Expense e " +
                "WHERE e.user.userId = :userId " +
                "AND e.category.categoryId = :categoryId " +
                "AND e.expenseDate BETWEEN :startDate AND :endDate")
        BigDecimal calculateTotalExpense(
                @Param("userId") Long userId,
                @Param("categoryId") Long categoryId,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate);
}