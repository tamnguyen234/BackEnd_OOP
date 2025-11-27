package com.javaproject.Backend.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportReponse.ExpenseReportRow;
import com.javaproject.Backend.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/expense/current")
    public List<ExpenseReportRow> generateReport(@Valid @RequestBody ReportRequest request) {
        // Validate tháng/năm
        int month;
        int year;
        try {
            month = Integer.parseInt(request.getMonth());
            year = Integer.parseInt(request.getYear());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Month and Year must be numbers");
        }

        // Kiểm tra tháng hợp lệ 1-12
        if (month < 1 || month > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Month must be between 1 and 12");
        }

        YearMonth requestYM = YearMonth.of(year, month);
        YearMonth currentYM = YearMonth.now();
        YearMonth threeMonthsAgo = currentYM.minusMonths(3);

        // Không được sau tháng hiện tại
        if (requestYM.isAfter(currentYM)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Month cannot be after the current month");
        }

        // Chỉ được trước tối đa 3 tháng
        if (requestYM.isBefore(threeMonthsAgo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Month cannot be more than 3 months before the current month");
        }

        // Nếu hợp lệ, tiếp tục xử lý report
        return reportService.getExpenseReport(request);
    }
}

// // ==== Endpoint tạo report ====
// @PostMapping("/generate")
// public ResponseEntity<ReportResponse> generate(@Valid @RequestBody
// ReportRequest req) {
// return ResponseEntity.ok(reportService.generateReport(req));
// }
