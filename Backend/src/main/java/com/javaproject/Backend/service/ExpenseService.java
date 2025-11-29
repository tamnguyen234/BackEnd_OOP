package com.javaproject.Backend.service;

import java.time.LocalDate;
import java.util.List;

import com.javaproject.Backend.dto.request.ExpenseRequest;
import com.javaproject.Backend.dto.request.update.ExpenseUpdateRequest;
import com.javaproject.Backend.dto.response.ExpenseResponse;

/**
 * Interface định nghĩa các dịch vụ (Service) liên quan đến Quản lý Chi tiêu (Expense).
 * * Chịu trách nhiệm thực hiện logic nghiệp vụ cho việc tạo, truy xuất, cập nhật, và xóa Chi tiêu.
 * * Các phương thức 'My' sử dụng ID của người dùng hiện tại (từ Security Context).
 */
public interface ExpenseService {

    /**
     * Tạo một giao dịch Chi tiêu mới (Expense) cho người dùng hiện tại.
     * * Phương thức này chịu trách nhiệm: Chuyển đổi DTO sang Entity, tìm Category,
     * kiểm tra ràng buộc (nếu có), và lưu vào DB.
     * @param request Dữ liệu Chi tiêu cần tạo từ client.
     * @return ExpenseResponse của giao dịch vừa được tạo.
     */
    ExpenseResponse createExpense(ExpenseRequest request);

    /**
     * Truy xuất tất cả các giao dịch Chi tiêu của người dùng hiện tại.
     * * Lấy User ID từ Security Context.
     * @return Danh sách các ExpenseResponse của người dùng.
     */
    List<ExpenseResponse> getMyExpenses();
    
    /**
     * Truy xuất tất cả các giao dịch Chi tiêu của một người dùng cụ thể.
     * * Thường được sử dụng cho mục đích quản trị hoặc các logic nội bộ.
     * @param userId ID của người dùng cần truy xuất Chi tiêu.
     * @return Danh sách các ExpenseResponse.
     */
    List<ExpenseResponse> getExpensesByUser(Long userId);

    /**
     * Truy xuất các giao dịch Chi tiêu của người dùng hiện tại trong một khoảng thời gian cụ thể.
     * * Lấy User ID từ Security Context.
     * @param start Ngày bắt đầu khoảng thời gian.
     * @param end Ngày kết thúc khoảng thời gian.
     * @return Danh sách các ExpenseResponse trong khoảng thời gian đã chọn.
     */
    List<ExpenseResponse> getMyExpensesBetween(LocalDate start, LocalDate end);
    
    /**
     * Truy xuất các giao dịch Chi tiêu của một người dùng cụ thể trong một khoảng thời gian.
     * @param userId ID của người dùng cần truy xuất.
     * @param start Ngày bắt đầu khoảng thời gian.
     * @param end Ngày kết thúc khoảng thời gian.
     * @return Danh sách các ExpenseResponse trong khoảng thời gian đã chọn.
     */
    List<ExpenseResponse> getExpensesByUserBetween(Long userId, LocalDate start, LocalDate end);

    /**
     * Cập nhật thông tin của một giao dịch Chi tiêu đã tồn tại.
     * * Đảm bảo người dùng hiện tại có quyền sở hữu Chi tiêu trước khi cập nhật.
     * @param expenseId ID của Chi tiêu cần cập nhật.
     * @param request Dữ liệu cập nhật.
     * @return ExpenseResponse sau khi cập nhật thành công.
     */
    ExpenseResponse updateExpense(Long expenseId, ExpenseUpdateRequest request);

    /**
     * Xóa một giao dịch Chi tiêu dựa trên ID.
     * * Đảm bảo người dùng hiện tại có quyền sở hữu Chi tiêu trước khi xóa.
     * @param expenseId ID của Chi tiêu cần xóa.
     */
    void deleteExpense(Long expenseId);
}