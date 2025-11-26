package com.javaproject.Backend.service.impl;

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
import com.javaproject.Backend.repository.CategoryRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.BudgetService;
import com.javaproject.Backend.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    // ==== Tạo một ngân sách mới (Budget) ====
    @Override // Triển khai phương thức từ interface BudgetService
    public BudgetResponse createBudget(BudgetRequest request) {
        User user = userRepository.findById(userService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Budget b = Budget.builder()
                .user(user)
                .category(category)
                .amountLimit(request.getAmountLimit())
                .period(request.getPeriod())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        // Lưu đối tượng vào database
        Budget saved = budgetRepository.save(b);

        return map(saved);
    }

    // ==== Truy xuất danh sách Ngân sách theo userId =====
    @Override // Triển khai phương thức từ interface BudgetService
    public List<BudgetResponse> getBudgetsByUser(Long userId) {
        return budgetRepository.findByUserUserId(userId).stream()
                .map(this::map).collect(Collectors.toList());
    }

    // ==== Logic truy cập dữ liệu cá nhân cho Budget ====
    @Override
    public List<BudgetResponse> getMyBudgets() {
        Long currentUserId = userService.getCurrentUserId();

        // 3. Gọi phương thức truy vấn
        return getBudgetsByUser(currentUserId);
    }

    /** CẬP NHẬT Budget (PUT/PATCH) **/
    public BudgetResponse updateBudget(Long budgetId, BudgetUpdateRequest request) {
        Long currentUserId = userService.getCurrentUserId();

        // 1. Tìm kiếm và kiểm tra quyền sở hữu
        Budget budget = budgetRepository.findByBudgetIdAndUserUserId(budgetId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found or access denied."));

        // 2. Cập nhật các thuộc tính (Non-Null/Non-Blank Update)

        // Amount (Numeric - chỉ cần kiểm tra != null)
        if (request.getAmountLimit() != null) {
            budget.setAmountLimit(request.getAmountLimit());
        }

        // StartDate (Date/Time - chỉ cần kiểm tra != null)
        if (request.getStartDate() != null) {
            budget.setStartDate(request.getStartDate());
        }

        // EndDate (Date/Time - chỉ cần kiểm tra != null)
        if (request.getEndDate() != null) {
            budget.setEndDate(request.getEndDate());
        }

        if (request.getCategoryId() != null) {
            Category newCategory = categoryRepository
                    .findByCategoryIdAndUserUserId(request.getCategoryId(), currentUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found or access denied."));
            budget.setCategory(newCategory);
        }

        // 3. Lưu (Update) vào Database
        Budget updatedBudget = budgetRepository.save(budget);

        // 4. Ánh xạ (Map) sang Response DTO và trả về
        return map(updatedBudget);
    }

    /** XÓA Budget **/
    public void deleteBudget(Long budgetId) {
        Long currentUserId = userService.getCurrentUserId();

        // 1. Tìm kiếm và kiểm tra quyền sở hữu
        Budget budget = budgetRepository.findByBudgetIdAndUserUserId(budgetId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found or access denied."));

        // 2. Xóa khỏi Database
        budgetRepository.delete(budget);
    }

    // hàm map hỗ trợ: chuyển đổi đối tượng budget đã lưu thành BudgetResponse
    private BudgetResponse map(Budget b) {
        return BudgetResponse.builder()
                .budgetId(b.getBudgetId())
                // .userId(b.getUser().getUserId())
                .categoryId(b.getCategory().getCategoryId())
                .amountLimit(b.getAmountLimit())
                .period(b.getPeriod())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .build();
    }
}
