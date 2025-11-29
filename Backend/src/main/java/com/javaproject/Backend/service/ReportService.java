package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportReponse.ExpenseReportRow;

/**
 * Interface định nghĩa các dịch vụ (Service) liên quan đến Tạo và Quản lý Báo cáo (Report).
 * * Chịu trách nhiệm thực hiện logic nghiệp vụ phức tạp để tổng hợp dữ liệu chi tiêu
 * và ngân sách thành các báo cáo có ý nghĩa.
 */
public interface ReportService {

    /**
     * Lấy báo cáo chi tiêu cho người dùng hiện tại, dựa trên tháng và năm được yêu cầu.
     * * Phương thức này thực hiện logic kiểm tra: Nếu báo cáo đã được lưu trữ (archived)
     * thì trả về dữ liệu lưu trữ (cached), ngược lại thì tạo báo cáo theo thời gian thực (real-time)
     * và lưu trữ kết quả.
     * @param request Yêu cầu báo cáo, chứa tháng và năm cần xem.
     * @return Danh sách các hàng báo cáo (ExpenseReportRow) tóm tắt chi tiêu theo danh mục.
     */
    List<ExpenseReportRow> getExpenseReport(ReportRequest request);

    /**
     * Tạo báo cáo chi tiêu theo thời gian thực cho một người dùng cụ thể.
     * * Phương thức này tổng hợp chi tiêu, so sánh tổng chi tiêu với giới hạn Ngân sách (Budget Limit),
     * và trả về kết quả chi tiết.
     * * Thường được gọi nội bộ bởi getExpenseReport hoặc các tác vụ nền.
     * @param userId ID của người dùng cần tạo báo cáo.
     * @param request Yêu cầu báo cáo, chứa tháng và năm.
     * @return Danh sách các hàng báo cáo (ExpenseReportRow) đã được tổng hợp.
     */
    List<ExpenseReportRow> generateExpenseReportForCurrentUser(Long userId, ReportRequest request);
}