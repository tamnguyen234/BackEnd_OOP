package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportReponse.ExpenseReportRow;

public interface ReportService {
    // List Report: Sum (chi) compare limit:
    List<ExpenseReportRow> generateExpenseReportForCurrentUser(ReportRequest request);
    // // ==== TẠO BÁO CÁO (generateReport) ====
    // ReportResponse generateReport(ReportRequest request);

    void scheduledMonthlyReport();

    List<ExpenseReportRow> generateExpenseReport(Long userId, ReportRequest request);

    // // ==== BÁO CÁO ĐỊNH KỲ HÀNG THÁNG ====
    // void scheduledMonthlyReport(); // gọi bởi scheduler
}