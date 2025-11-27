package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportReponse.ExpenseReportRow;

public interface ReportService {
    // So sánh tháng và năm để reponce: gen realtime or get data
    List<ExpenseReportRow> getExpenseReport(ReportRequest request);

    // List Report: Sum (chi) compare limit:
    List<ExpenseReportRow> generateExpenseReportForCurrentUser(Long userId, ReportRequest request);
}