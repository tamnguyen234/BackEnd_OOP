package com.javaproject.Backend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.Expense;
import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.ExpenseRequest;
import com.javaproject.Backend.dto.request.update.ExpenseUpdateRequest;
import com.javaproject.Backend.dto.response.ExpenseResponse;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.CategoryRepository;
import com.javaproject.Backend.repository.ExpenseRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.CategoryService;
import com.javaproject.Backend.service.ExpenseService;
import com.javaproject.Backend.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final CategoryService categoryService; // Sử dụng CategoryService
    
    // ====== Tạo Khoản Chi Mới =====
    @Override
    @Transactional // Thêm Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        
        // 1. Tìm User
        User user = userRepository.findById(userService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // 2. **Sử dụng CategoryService để lấy Category Proxy** (Clean code!)
        Category categoryReference = categoryService.getReferenceByNameAndType(
            request.getCategoryName(),
            request.getCategoryType()
        );

        // 3. Tạo đối tượng Expense
        Expense e = Expense.builder()
            .user(user)
            .category(categoryReference) // <-- Đúng kiểu dữ liệu: Category
            .amount(request.getAmount())
            .description(request.getDescription())
            .expenseDate(request.getExpenseDate())
            .build();

        Expense saved = expenseRepository.save(e);
        return mapToResponse(saved);
    }


        // ==== Logic truy cập dữ liệu cá nhân cho Expense ====
    @Override
    public List<ExpenseResponse> getMyExpenses() {  
        Long currentUserId = userService.getCurrentUserId();
        
        // 3. Gọi phương thức truy vấn
        return getExpensesByUser(currentUserId);
    }
    // ==== Truy Vấn Tất Cả Chi Tiêu của Người Dùng =====
    @Override
    public List<ExpenseResponse> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserUserId(userId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

     // ==== Logic truy cập dữ liệu cá nhân theo khoảng thời gian ====
    @Override
    public List<ExpenseResponse> getMyExpensesBetween(LocalDate start, LocalDate end) {
        Long currentUserId = userService.getCurrentUserId();
        
        // 3. Gọi phương thức truy vấn cũ (giờ đã an toàn vì userId được xác thực)
        return getExpensesByUserBetween(currentUserId, start, end);
    }
    
    // Phương thức truy vấn chung (triển khai dựa trên Repository)
    @Override
    public List<ExpenseResponse> getExpensesByUserBetween(Long userId, LocalDate start, LocalDate end) {
        // Phương thức này CẦN được triển khai trong ExpenseRepository
        return expenseRepository.findByUserUserIdAndExpenseDateBetween(userId, start, end)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(Long expenseId, ExpenseUpdateRequest request) {
        Long currentUserId = userService.getCurrentUserId(); 

        // 1. Tìm kiếm và kiểm tra quyền sở hữu
        Expense expense = expenseRepository.findByExpenseIdAndUserUserId(expenseId, currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found or access denied."));
        
        // 2. Cập nhật các thuộc tính cơ bản
        // Description
        if (StringUtils.hasText(request.getDescription())) {
            expense.setDescription(request.getDescription());
        }
        
        // Amount
        if (request.getAmount() != null) {
            expense.setAmount(request.getAmount());
        }
        // Date
        if (request.getExpenseDate() != null) {
            expense.setExpenseDate(request.getExpenseDate());
        }
        // 3. Xử lý Cập nhật Category (Name/Type)
        String newName = request.getCategoryName();
        String newType = request.getCategoryType();
        // Case 1: Cập nhật Category Name và Type (Nếu Type KHÔNG null)
        if (StringUtils.hasText(newName) && StringUtils.hasText(newType)) {
            // Cập nhật cả Name và Type: Tìm Category mới theo cặp (Name, Type)
            Category newCategory = categoryService.getReferenceByNameAndType(newName, newType);
            expense.setCategory(newCategory);
            
        } 
        // Case 2: Chỉ cập nhật Category Name (Nếu Type LÀ null)
        else if (StringUtils.hasText(newName) && newType == null) {
            // Lấy Type hiện tại của Expense để tìm Category mới theo (Name mới, Type cũ)
            String currentType = expense.getCategory().getType(); 
            Category newCategory = categoryService.getReferenceByNameAndType(newName, currentType);
            expense.setCategory(newCategory);

        }
        // Case 3: Type CÓ, Name KHÔNG (Bắt lỗi theo yêu cầu)
        else if (newType != null && !StringUtils.hasText(newName)) {
            // Nếu có Type mới nhưng không có Name mới: Trả về lỗi yêu cầu người dùng nhập Name.
            throw new IllegalArgumentException("Category Name is required when attempting to update Category Type.");
            
        }
        
        // 4. Lưu (Update) vào Database
        Expense updatedExpense = expenseRepository.save(expense);
        
        // 5. Ánh xạ (Map) sang Response DTO và trả về
        return mapToResponse(updatedExpense);
    }

    /** XÓA Expense **/
    public void deleteExpense(Long expenseId) {
        Long currentUserId = userService.getCurrentUserId();

        // 1. Tìm kiếm và kiểm tra quyền sở hữu
        Expense expense = expenseRepository.findByExpenseIdAndUserUserId(expenseId, currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found or access denied."));
        
        // 2. Xóa khỏi Database
        expenseRepository.delete(expense);
    }

    private ExpenseResponse mapToResponse(Expense e) {
        Category category = e.getCategory();

        return ExpenseResponse.builder()
            .expenseId(e.getExpenseId())
            .CategoryName(category.getName())             
            .CategoryType(category.getType())            
            .amount(e.getAmount())
            .description(e.getDescription())
            .expenseDate(e.getExpenseDate())
            .build();
    }
}
