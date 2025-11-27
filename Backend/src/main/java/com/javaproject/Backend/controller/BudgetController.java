package com.javaproject.Backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.request.update.BudgetUpdateRequest;
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
    @PostMapping("/create")
    public ResponseEntity<BudgetResponse> create(@Valid @RequestBody BudgetRequest req) {
        return ResponseEntity.ok(budgetService.createBudget(req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BudgetResponse>> getMyBudgets() {
        return ResponseEntity.ok(budgetService.getMyBudgets());
    }

    /** CẬP NHẬT: PUT /api/expenses/{id} **/
    @PutMapping("/update/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(@PathVariable Long id,
            @RequestBody BudgetUpdateRequest request) {

        BudgetResponse updatedBudget = budgetService.updateBudget(id, request);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {

        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
