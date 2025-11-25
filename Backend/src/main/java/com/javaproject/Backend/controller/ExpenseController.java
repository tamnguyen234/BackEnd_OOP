package com.javaproject.Backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.ExpenseRequest;
import com.javaproject.Backend.dto.request.update.ExpenseUpdateRequest;
import com.javaproject.Backend.dto.response.ExpenseResponse;
import com.javaproject.Backend.service.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
// Tự động sinh constructor cho các field final (ở đây là expenseService)
public class ExpenseController {
    private final ExpenseService expenseService;
    // ===== Endpoint tạo Expense mới =====
    // POST
    @PostMapping("/create")
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest req) {
        return ResponseEntity.ok(expenseService.createExpense(req));
    }
    // ===== Endpoint lấy danh sách Expense theo user =====
    // GET 
    // @GetMapping("/user/{userId}")
    // public ResponseEntity<List<ExpenseResponse>> getByUser(@PathVariable Long userId) {
    //     // @PathVariable: lấy giá trị userId từ URL
    //     return ResponseEntity.ok(expenseService.getExpensesByUser(userId));
    // }
    // ==== Endpoint truy xuất Expense của người dùng đang đăng nhập (SỬA ĐỔI) ====
    // Endpoint mới: GET /api/expenses (Lấy ID từ Token)
    @GetMapping("/my")
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses() {
        return ResponseEntity.ok(expenseService.getMyExpenses());
    }
     // ===== Endpoint lấy danh sách Expense theo user trong khoảng thời gian =====
    // @GetMapping("/user/{userId}/between")
    // public ResponseEntity<List<ExpenseResponse>> getByUserBetween(
    //         @PathVariable Long userId,
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    //         // @DateTimeFormat: chuyển dạng date string sang LocalDate theo ISO 
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
    //     return ResponseEntity.ok(expenseService.getExpensesByUserBetween(userId, start, end));
    // }
    // Endpoint mới: GET /api/expenses/between?start=...&end=...
    @GetMapping("/between")
    public ResponseEntity<List<ExpenseResponse>> getMyExpensesBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        
        // Gọi Service mới (không cần truyền userId)
        return ResponseEntity.ok(expenseService.getMyExpensesBetween(start, end));
    }
    /** CẬP NHẬT: PUT /api/expenses/{id} **/
    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id, 
                                                       @RequestBody ExpenseUpdateRequest request) {
        
        ExpenseResponse updatedExpense = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(updatedExpense);
    }

    /** XÓA: DELETE /api/expenses/{id} **/
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build(); 
    }
}
// ResponseEntity là một class trong Spring Framework 
// (thuộc package org.springframework.http)
//  dùng để đóng gói HTTP response trả về từ controller.