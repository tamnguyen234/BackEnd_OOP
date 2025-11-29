package com.javaproject.Backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaproject.Backend.domain.Budget;

import jakarta.transaction.Transactional;

/**
 * Interface Repository chịu trách nhiệm tương tác với cơ sở dữ liệu cho thực thể Budget.
 * * Mở rộng JpaRepository để thừa hưởng các phương thức CRUD cơ bản.
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    /**
     * Tìm và trả về tất cả các Ngân sách (Budget) thuộc về một người dùng cụ thể.
     * @param userId ID của người dùng.
     * @return Danh sách các Budget thuộc về userId đó.
     */
    List<Budget> findByUserUserId(Long userId);

    /**
     * Tìm một Ngân sách theo ID của nó, đảm bảo rằng nó thuộc về người dùng có userId tương ứng.
     * * Dùng để kiểm tra quyền sở hữu trước khi thực hiện các thao tác Sửa/Xóa.
     * @param budgetId ID của Ngân sách.
     * @param userId ID của người dùng.
     * @return Optional chứa Budget nếu tìm thấy, ngược lại là Optional.empty().
     */
    Optional<Budget> findByBudgetIdAndUserUserId(Long budgetId, Long userId);

    /**
     * Tìm tất cả Ngân sách của một người dùng mà còn hiệu lực trong một khoảng thời gian cụ thể.
     * * Logic kiểm tra: (Budget.startDate <= endDate) AND (Budget.endDate >= startDate).
     * * Điều này đảm bảo Budget có sự chồng lấn với khoảng [startDate, endDate] truyền vào.
     * @param userId ID của người dùng.
     * @param endDate Ngày kết thúc của khoảng thời gian cần kiểm tra.
     * @param startDate Ngày bắt đầu của khoảng thời gian cần kiểm tra.
     * @return Danh sách các Budget đang hoạt động trong khoảng thời gian đó.
     */
    List<Budget> findByUserUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long userId, LocalDate endDate, LocalDate startDate);
    /**
     * Kiểm tra xem một Ngân sách có tồn tại hay không, và phải thuộc về người dùng cụ thể.
     * * Tránh việc tải toàn bộ đối tượng, tối ưu hơn cho các kiểm tra điều kiện.
     * @param budgetId ID của Ngân sách.
     * @param userId ID của người dùng.
     * @return true nếu tồn tại, ngược lại là false.
     */
    boolean existsByBudgetIdAndUserUserId(Long budgetId, Long userId);

    /**
     * Xóa tất cả các Ngân sách đã hết hạn (endDate nhỏ hơn ngày truyền vào) của một người dùng.
     * * Sử dụng cho các tác vụ dọn dẹp theo lịch (scheduling).
     * @param userId ID của người dùng sở hữu các Ngân sách cần xóa.
     * @param endDate Ngày mốc để so sánh (các Budget có endDate trước ngày này sẽ bị xóa).
     *
     * * @Modifying: Cho biết đây là truy vấn sửa đổi dữ liệu (DELETE, UPDATE, INSERT).
     * * @Transactional: Đảm bảo thao tác này được thực hiện trong một Transaction.
     * * @Query: JPQL để thực hiện lệnh DELETE.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Budget b WHERE b.user.id = :userId AND b.endDate < :endDate")
    void deleteExpiredBudgetsByUserId(@Param("userId") Long userId, @Param("endDate") LocalDate endDate);
}