package com.javaproject.Backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;
import com.javaproject.Backend.service.BudgetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;
    // ==== Endpoint tạo ngân sách ====
    @PostMapping
    public ResponseEntity<BudgetResponse> create(@Valid @RequestBody BudgetRequest req) {
        return ResponseEntity.ok(budgetService.createBudget(req));
    }
    // // ==== Endpoint truy xuất ngân sách
    // @GetMapping("/user/{userId}")
    // public ResponseEntity<List<BudgetResponse>> getByUser(@PathVariable Long userId) {
    //     return ResponseEntity.ok(budgetService.getBudgetsByUser(userId));
    // }
    // ==== Endpoint truy xuất Budget của người dùng đang đăng nhập (SỬA ĐỔI) ====
    // Endpoint mới: GET /api/budgets (Lấy ID từ Token)
    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getMyBudgets() {
        return ResponseEntity.ok(budgetService.getMyBudgets());
    }
}
