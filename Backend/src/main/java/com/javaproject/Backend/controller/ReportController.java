package com.javaproject.Backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportReponse.ExpenseReportRow;
import com.javaproject.Backend.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/expense/current")
    public ResponseEntity<List<ExpenseReportRow>> getExpenseReportForCurrentUser(@RequestBody ReportRequest request) {
        List<ExpenseReportRow> report = reportService.generateExpenseReportForCurrentUser(request);
        return ResponseEntity.ok(report);
    }
    // // ==== Endpoint tạo report ====
    // @PostMapping("/generate")
    // public ResponseEntity<ReportResponse> generate(@Valid @RequestBody
    // ReportRequest req) {
    // return ResponseEntity.ok(reportService.generateReport(req));
    // }
}
