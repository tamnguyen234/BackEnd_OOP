package com.javaproject.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.domain.Expense;
/**
 * Lớp chứa các định nghĩa Interface Repository.
 */
public class ReportRepository {
    /**
     * Interface Repository chịu trách nhiệm truy cập dữ liệu cho thực thể Chi tiêu (Expense).
     * * @Repository: Đánh dấu là Spring Component cho tầng Persistence (dữ liệu).
     * * Mở rộng JpaRepository để thừa hưởng các phương thức CRUD cơ bản.
     */
    @Repository
    public interface ExpenseRepository extends JpaRepository<Expense, Long> {
        /**
         * Tìm và trả về danh sách Chi tiêu thuộc về một người dùng cụ thể.
         * * Phương thức truy vấn được tạo tự động (Derived Query Method).
         * @param userId ID của người dùng.
         * @return Danh sách các giao dịch Chi tiêu (List<Expense>).
         */
        List<Expense> findByUserUserId(Long userId);
    }

    public interface BudgetRepository extends JpaRepository<Budget, Long> {
    }

}
