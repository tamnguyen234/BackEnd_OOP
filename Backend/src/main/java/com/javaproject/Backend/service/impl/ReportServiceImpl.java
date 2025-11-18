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

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ExpenseRepository expenseRepository;

    @Override
    public ReportResponse generateReport(ReportRequest request) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();
        if (start == null || end == null) {
            // default: this month
            LocalDate now = LocalDate.now();
            start = now.withDayOfMonth(1);
            end = now;
        }
        List<com.javaproject.Backend.domain.Expense> expenses;
        if (request.getUserId() != null) {
            expenses = expenseRepository.findByUserUserIdAndExpenseDateBetween(request.getUserId(), start, end);
        } else {
            expenses = expenseRepository.findByExpenseDateBetween(start, end);
        }
        BigDecimal total = expenses.stream()
                .map(com.javaproject.Backend.domain.Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ReportResponse.builder()
                .userId(request.getUserId())
                .startDate(start)
                .endDate(end)
                .totalExpense(total)
                .build();
    }

    @Override
    public void scheduledMonthlyReport() {
        // Example: compute company-wide total for last month
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusMonths(1).withDayOfMonth(1);
        LocalDate end = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth());
        ReportRequest r = ReportRequest.builder().startDate(start).endDate(end).build();
        ReportResponse resp = generateReport(r);
        // TODO: save to DB or send email. For now, just print
        System.out.println("Scheduled monthly report: " + resp.toString());
    }
}
