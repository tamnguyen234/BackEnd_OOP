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

/**
 * Controller xử lý các yêu cầu HTTP liên quan đến Quản lý Chi tiêu (Expense).
 * * Các endpoint đều yêu cầu người dùng đã được xác thực (authenticated).
 */
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    /**
     * Endpoint tạo một giao dịch Chi tiêu mới cho người dùng hiện tại.
     * * PATH: /api/expenses/create
     * @param req Dữ liệu Chi tiêu mới từ client (@Valid kích hoạt validation).
     * @return ResponseEntity chứa ExpenseResponse của Chi tiêu vừa được tạo.
     */
    @PostMapping("/create")
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest req) {
        return ResponseEntity.ok(expenseService.createExpense(req));
    }

    /**
     * Endpoint lấy danh sách tất cả các giao dịch Chi tiêu của người dùng hiện tại.
     * * PATH: /api/expenses/my
     * @return ResponseEntity chứa List<ExpenseResponse> của các chi tiêu.
     */
    @GetMapping("/my")
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses() {
        return ResponseEntity.ok(expenseService.getMyExpenses());
    }

    /**
     * Endpoint lấy danh sách Chi tiêu của người dùng hiện tại trong một khoảng thời gian cụ thể.
     * * PATH: /api/expenses/between?start=YYYY-MM-DD&end=YYYY-MM-DD
     * @param start Ngày bắt đầu.
     * @param end Ngày kết thúc.
     * @return ResponseEntity chứa List<ExpenseResponse> trong khoảng thời gian đã chọn.
     */
    @GetMapping("/between")
    public ResponseEntity<List<ExpenseResponse>> getMyExpensesBetween(
            // @RequestParam: đánh dấu đây là tham số từ URL Query
            // @DateTimeFormat: đảm bảo Spring parse chuỗi thành LocalDate theo format ISO.DATE
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(expenseService.getMyExpensesBetween(start, end));
    }
    
    /**
     * Endpoint cập nhật thông tin chi tiết của một giao dịch Chi tiêu cụ thể.
     * * PATH: /api/expenses/update/{id}
     * @param id ID của Chi tiêu cần cập nhật (lấy từ Path Variable).
     * @param request Dữ liệu cập nhật (lấy từ Request Body).
     * @return ResponseEntity chứa ExpenseResponse đã được cập nhật.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id,
            @RequestBody ExpenseUpdateRequest request) {

        ExpenseResponse updatedExpense = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(updatedExpense);
    }

    /**
     * Endpoint xóa một giao dịch Chi tiêu dựa trên ID.
     * * METHOD: DELETE
     * * PATH: /api/expenses/delete/{id}
     * @param id ID của Chi tiêu cần xóa (lấy từ Path Variable).
     * @return ResponseEntity với trạng thái HTTP 204 No Content (thành công nhưng không trả về nội dung).
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {

        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
