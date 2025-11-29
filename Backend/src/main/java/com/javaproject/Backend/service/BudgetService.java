package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.request.update.BudgetUpdateRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;

/**
 * Interface định nghĩa các dịch vụ (Service) liên quan đến Quản lý Ngân sách (Budget).
 * * Chịu trách nhiệm thực hiện logic nghiệp vụ cho việc tạo, truy xuất, cập nhật, và xóa Ngân sách.
 */
public interface BudgetService {

    /**
     * Tạo một Ngân sách mới cho một người dùng cụ thể.
     * * Phương thức này thường được dùng cho việc tạo Ngân sách thủ công bởi người dùng.
     * @param userId ID của người dùng sở hữu Ngân sách.
     * @param request Dữ liệu Ngân sách cần tạo (giới hạn, danh mục, thời gian).
     * @return Đối tượng Budget đã được lưu vào cơ sở dữ liệu.
     */
    Budget createBudget(Long userId ,BudgetRequest request);

    /**
     * Tự động tạo các Ngân sách mặc định hàng tháng (ví dụ: cho tháng hiện tại)
     * cho một người dùng mới.
     * * Phương thức này được gọi khi có sự kiện UserCreatedEvent (qua Event Listener).
     * @param userId ID của người dùng cần khởi tạo Ngân sách mặc định.
     */
    void createMonthlyDefaultBudgets(Long userId);

    /**
     * Truy xuất danh sách tất cả các Ngân sách của người dùng hiện tại (dựa trên Security Context).
     * @return Danh sách các BudgetResponse của người dùng.
     */
    List<BudgetResponse> getMyBudgets();

    /**
     * Cập nhật thông tin của một Ngân sách đã tồn tại.
     * * Đảm bảo người dùng hiện tại có quyền sở hữu Ngân sách trước khi cập nhật.
     * @param budgetId ID của Ngân sách cần cập nhật.
     * @param request Dữ liệu cập nhật (ví dụ: giới hạn số tiền mới).
     * @return BudgetResponse sau khi cập nhật thành công.
     */
    BudgetResponse updateBudget(Long budgetId, BudgetUpdateRequest request);

    /**
     * Xóa một Ngân sách dựa trên ID.
     * * Đảm bảo người dùng hiện tại có quyền sở hữu Ngân sách trước khi xóa.
     * @param budgetId ID của Ngân sách cần xóa.
     */
    void deleteBudget(Long budgetId);

}