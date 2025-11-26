package com.javaproject.Backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportResponse;
import com.javaproject.Backend.repository.ExpenseRepository;
import com.javaproject.Backend.service.ReportService;

import lombok.RequiredArgsConstructor;

// Đánh dấu lớp này là Service và sử dụng Dependency Injection thông qua RequiredArgsConstructor
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ExpenseRepository expenseRepository;

    // ==== TẠO BÁO CÁO (generateReport) ====
    @Override // Triển khai phương thức từ interface ReportService.
    public ReportResponse generateReport(ReportRequest request) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();
        if (start == null || end == null) {
            // thiết lập mặc định là TỪ ĐẦU THÁNG HIỆN TẠI đến HÔM NAY.
            LocalDate now = LocalDate.now();
            start = now.withDayOfMonth(1); // ngày 1 tháng hiện tại
            end = now;
        }
        // Danh sách để lưu trữ các đối tượng Chi tiêu (Expense)
        List<com.javaproject.Backend.domain.Expense> expenses;
        if (request.getUserId() != null) {
            // Báo cáo chi tiêu 1 người dùng nhất định theo UserID
            expenses = expenseRepository.findByUserUserIdAndExpenseDateBetween(request.getUserId(), start, end);
        } else {
            // Báo cáo toàn hệ thống
            expenses = expenseRepository.findByExpenseDateBetween(start, end);
        }

        // Tổng chi tiêu
        BigDecimal total = expenses.stream()
                // Lấy trường 'amount' (số tiền) từ mỗi đối tượng Expense
                .map(com.javaproject.Backend.domain.Expense::getAmount)
                // Cộng tổng các số tiền lại, bắt đầu từ 0 (BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ReportResponse.builder()
                // .userId(request.getUserId())
                .startDate(start)
                .endDate(end)
                .totalExpense(total) // Tổng chi tiêu
                .build();
    }

    // ==== BÁO CÁO ĐỊNH KỲ HÀNG THÁNG ====
    @Override
    public void scheduledMonthlyReport() {
        // Lấy ngày hiện tại
        LocalDate now = LocalDate.now();
        // Ngày bắt đầu: Ngày 1 của tháng trước
        LocalDate start = now.minusMonths(1) // trừ đi 1 tháng so với ngày được gọi
                .withDayOfMonth(1);// thiết lập này về ngày int nhập vào ở đây là 1. Trả về đối tượng localdate mới
        // Ngày kết thúc: Ngày cuối cùng của tháng trước
        LocalDate end = now.minusMonths(1)
                .withDayOfMonth(now.minusMonths(1).lengthOfMonth()); // trả về số ngày tháng trước
        ReportRequest r = ReportRequest.builder().startDate(start).endDate(end).build();
        ReportResponse resp = generateReport(r);
        // Trong môi trường thực tế:
        // - Lưu báo cáo vào DB (cho lịch sử).
        // - Gửi email cho quản lý.
        // - Cập nhật dashboard.
        // in ra console để kiểm tra
        System.out.println("Scheduled monthly report: " + resp.toString());
    }
}
