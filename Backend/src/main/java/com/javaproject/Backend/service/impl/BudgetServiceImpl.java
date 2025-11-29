package com.javaproject.Backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.request.update.BudgetUpdateRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.BudgetRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.BudgetService;
import com.javaproject.Backend.service.CategoryService;
import com.javaproject.Backend.service.UserService;
import com.javaproject.Backend.repository.ExpenseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Triển khai (Implementation) của BudgetService, xử lý logic nghiệp vụ cho Quản lý Ngân sách.
 * * @Service: Đánh dấu class này là Service Component của Spring.
 * * @RequiredArgsConstructor: Tự động tạo constructor với các trường final (Dependency Injection).
 */
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ExpenseRepository expenseRepository;

    private static final List<String> DEFAULT_EXPENSE_CATEGORIES = Arrays.asList(
            "Ăn uống", "Di chuyển", "Giải trí", "Tiện ích",
            "Tiết kiệm", "Sức khỏe", "Mua sắm", "Giáo dục", "Khác");
    private static final String DEFAULT_CATEGORY_TYPE = "Chi tiêu";
    private static final BigDecimal DEFAULT_AMOUNT_LIMIT = BigDecimal.ZERO;
    /**
     * Tạo các Ngân sách mặc định cho các danh mục Chi tiêu tiêu chuẩn trong tháng hiện tại
     * cho một người dùng mới.
     * * Phương thức này được gọi khi có sự kiện tạo User mới (UserCreatedEvent).
     */
    @Override
    @Transactional
    public void createMonthlyDefaultBudgets(Long userId) {

        // Tính toán ngày tháng cho tháng hiện tại
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        for (String categoryName : DEFAULT_EXPENSE_CATEGORIES) {
            // Gọi phương thức tạo cơ bản
            createBudget(userId, BudgetRequest.builder()
                    .CategoryName(categoryName)
                    .amountLimit(DEFAULT_AMOUNT_LIMIT)
                    .startDate(startDate)
                    .endDate(endDate).build());

        }
    }

    /**
     * Tạo một Ngân sách mới dựa trên request từ người dùng.
     */
    @Override
    @Transactional
    public Budget createBudget(Long userId, BudgetRequest request) {

        User user = userRepository.getReferenceById(userId);
        // 2. Tìm Category (Sử dụng Service)
        Category categoryReference = categoryService.getReferenceByNameAndType(
                request.getCategoryName(),
                DEFAULT_CATEGORY_TYPE);

        // 4. Xử lý Ngày (mặc định là đầu/cuối tháng hiện tại)
        LocalDate budgetStartDate = request.getStartDate();
        LocalDate budgetEndDate = request.getEndDate();

        // 5. Tạo đối tượng Budget
        Budget b = Budget.builder()
                .user(user)
                .category(categoryReference)
                .amountLimit(request.getAmountLimit())
                .startDate(budgetStartDate)
                .endDate(budgetEndDate)
                .build();

        // 6. Lưu và trả về
        Budget saved = budgetRepository.save(b);

        return saved;
    }
    /**
     * Lấy tất cả các Ngân sách của người dùng hiện tại.
     * * Phương thức này không cần @Transactional vì chỉ là thao tác đọc (read-only).
     */
    @Override
    public List<BudgetResponse> getMyBudgets() {
        Long currentUserId = userService.getCurrentUserId();

        return budgetRepository.findByUserUserId(currentUserId).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Cập nhật giới hạn số tiền của một Ngân sách.
     * * Phải kiểm tra quyền sở hữu (userId) trước khi cho phép cập nhật.
     */
    @Override
    @Transactional
    public BudgetResponse updateBudget(Long budgetId, BudgetUpdateRequest request) {
        Long currentUserId = userService.getCurrentUserId();

        // 1. Tìm Budget và kiểm tra quyền sở hữu
        Budget budget = budgetRepository.findByBudgetIdAndUserUserId(budgetId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found or access denied."));

        // 2. Cập nhật các thuộc tính Amount Limit
        if (request.getAmountLimit() != null) {
            budget.setAmountLimit(request.getAmountLimit());
        }
        // 3. Lưu và trả về
        Budget updatedBudget = budgetRepository.save(budget);
        return mapToResponse(updatedBudget);
    }
    /**
     * Xóa một Ngân sách dựa trên ID, sau khi kiểm tra quyền sở hữu.
     */
    @Override
    @Transactional
    public void deleteBudget(Long budgetId) {
        Long currentUserId = userService.getCurrentUserId();

        if (budgetRepository.existsByBudgetIdAndUserUserId(budgetId, currentUserId)) {
            budgetRepository.deleteById(budgetId);
        } else {
            throw new ResourceNotFoundException("Budget not found or access denied.");
        }
    }
    /**
     * Chuyển đổi đối tượng Budget Entity sang BudgetResponse DTO.
     * * Tính toán tổng chi tiêu hiện tại trong khoảng thời gian của Budget để cung cấp
     * thông tin trạng thái ngân sách.
     * @param b Đối tượng Budget Entity.
     * @return BudgetResponse DTO.
     */
    private BudgetResponse mapToResponse(Budget b) {
        Category category = b.getCategory();
        Long currentUserId = userService.getCurrentUserId();
        BigDecimal totalExpense = expenseRepository.calculateTotalExpense(
                currentUserId, category.getCategoryId(),
                b.getStartDate(), b.getEndDate());

        return BudgetResponse.builder()
                .budgetId(b.getBudgetId())
                .CategoryName(category.getName())
                .amountLimit(b.getAmountLimit())
                .totalExpense(totalExpense)
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .build();
    }
}
