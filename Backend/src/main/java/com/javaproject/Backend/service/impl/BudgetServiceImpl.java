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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private static final List<String> DEFAULT_EXPENSE_CATEGORIES = Arrays.asList(
    "Ăn uống", "Di chuyển", "Nhà ở", "Giải trí", 
    "Sức khỏe", "Học tập", "Tiết kiệm", "Quần áo", "Khác"
    );
    private static final String DEFAULT_CATEGORY_TYPE = "Chi tiêu"; 
    private static final BigDecimal DEFAULT_AMOUNT_LIMIT = BigDecimal.ZERO;
    
    
    @Override
    @Transactional
    public void createMonthlyDefaultBudgets(Long userId) {
        
        // Tính toán ngày tháng cho tháng hiện tại
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        for (String categoryName : DEFAULT_EXPENSE_CATEGORIES) {
            // Gọi phương thức tạo cơ bản (đã có)
            createBudget(BudgetRequest.builder()
                        .CategoryName(categoryName)
                        .amountLimit(DEFAULT_AMOUNT_LIMIT)
                        .startDate(startDate)
                        .endDate(endDate).build()
            );
            
        }
    }
    
    // ==== Tạo một ngân sách mới (Budget) ====
    @Override
    @Transactional
    public Budget createBudget(BudgetRequest request) {
        
        // 1. Tìm User hiện tại
        User user = userRepository.findById(userService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Tìm Category (Sử dụng Service)
        Category categoryReference = categoryService.getReferenceByNameAndType(
            request.getCategoryName(),
            DEFAULT_CATEGORY_TYPE
        );

        // 4. Xử lý Ngày (Nếu null, mặc định là đầu/cuối tháng hiện tại)
        LocalDate budgetStartDate = request.getStartDate();
        LocalDate budgetEndDate = request.getEndDate();

        // **Thêm kiểm tra ngày:** Đảm bảo ngày bắt đầu <= ngày kết thúc
        if (budgetStartDate.isAfter(budgetEndDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

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

    @Override
    // Không cần @Transactional vì đây là thao tác chỉ đọc (read-only)
    public List<BudgetResponse> getMyBudgets() {
        Long currentUserId = userService.getCurrentUserId();

        return budgetRepository.findByUserUserId(currentUserId).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BudgetResponse updateBudget(Long budgetId, BudgetUpdateRequest request) {
        Long currentUserId = userService.getCurrentUserId();

        // 1. Tìm Budget và kiểm tra quyền sở hữu
        Budget budget = budgetRepository.findByBudgetIdAndUserUserId(budgetId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found or access denied."));

        // 2. Cập nhật các thuộc tính (Non-Null Update)

        // Amount Limit
        if (request.getAmountLimit() != null) {
            budget.setAmountLimit(request.getAmountLimit());
        }

        // Start Date
        if (request.getStartDate() != null) {
            budget.setStartDate(request.getStartDate());
        }

        // End Date
        if (request.getEndDate() != null) {
            budget.setEndDate(request.getEndDate());
        }

        // Category (Cập nhật Category chỉ khi cả Name và Type được cung cấp)
        String newCategoryName = request.getCategoryName();

        if (newCategoryName != null) {
            // Sử dụng CategoryService để tìm Category mới
            Category newCategory = categoryService.getReferenceByNameAndType(newCategoryName, DEFAULT_CATEGORY_TYPE);
            
            // Kiểm tra logic nghiệp vụ: Budget chỉ cho phép loại "Chi tiêu" (EXPENSE)
            budget.setCategory(newCategory);
        }
        // 3. Lưu và trả về
        Budget updatedBudget = budgetRepository.save(budget);
        return mapToResponse(updatedBudget);
    }

    @Override
    @Transactional
    public void deleteBudget(Long budgetId) {
        Long currentUserId = userService.getCurrentUserId();

        // Hoặc bạn có thể dùng cách tối ưu hơn (tránh truy vấn SELECT ban đầu):
        if (budgetRepository.existsByBudgetIdAndUserUserId(budgetId, currentUserId)) {
            budgetRepository.deleteById(budgetId);
        } else {
            throw new ResourceNotFoundException("Budget not found or access denied.");
        }
    }

    private BudgetResponse mapToResponse(Budget b) {
        Category category = b.getCategory();

        return BudgetResponse.builder()
                .budgetId(b.getBudgetId())
                .CategoryName(category.getName())
                .amountLimit(b.getAmountLimit())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .build();
    }
}
