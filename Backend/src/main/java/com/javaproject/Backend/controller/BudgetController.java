package com.javaproject.Backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.update.BudgetUpdateRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;
import com.javaproject.Backend.service.BudgetService;

import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các yêu cầu HTTP liên quan đến Quản lý Ngân sách (Budget).
 * * Endpoint  yêu cầu người dùng đã được xác thực (authenticated).
 */
@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    /**
     * Endpoint lấy ngân sách của người dùng
     * * PATH: /api/budgets/my
     * @return ResponseEntity chứa List<BudgetResponse> của các ngân sách.
     */
    @GetMapping("/my")
    public ResponseEntity<List<BudgetResponse>> getMyBudgets() {
        return ResponseEntity.ok(budgetService.getMyBudgets());
    }

    /**
     * Endpoint Cập nhật thông tin của một Ngân sách.
     * * PATH: /api/budgets/update/{id}
     * @param id ID của Ngân sách cần cập nhật (lấy từ Path Variable).
     * @param request Dữ liệu cập nhật (lấy từ Request Body).
     * @return ResponseEntity chứa BudgetResponse đã được cập nhật.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(@PathVariable Long id,
            @RequestBody BudgetUpdateRequest request) {

        BudgetResponse updatedBudget = budgetService.updateBudget(id, request);
        return ResponseEntity.ok(updatedBudget);
    }
    /**
     * Endpoint xóa một Ngân sách.
     * * PATH: /api/budgets/delete/{id}
     * @param id ID của Ngân sách cần xóa (lấy từ Path Variable).
     * @return ResponseEntity với trạng thái HTTP 204 No Content (thành công nhưng không trả về nội dung).
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {

        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
