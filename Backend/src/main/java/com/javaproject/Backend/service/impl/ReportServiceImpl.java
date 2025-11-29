package com.javaproject.Backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.domain.Expense;
import com.javaproject.Backend.domain.ExpenseReportArchive;
import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportReponse.ExpenseReportRow;
import com.javaproject.Backend.repository.BudgetRepository;
import com.javaproject.Backend.repository.ExpenseReportArchiveRepository;
import com.javaproject.Backend.repository.ExpenseRepository;
import com.javaproject.Backend.service.ReportService;
import com.javaproject.Backend.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Triển khai (Implementation) của ReportService, xử lý logic nghiệp vụ cho việc tạo
 * Báo cáo Chi tiêu (Expense Report).
 * * @Service: Đánh dấu class này là Service Component của Spring.
 * * @RequiredArgsConstructor: Tự động tạo constructor với các trường final (Dependency Injection).
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
        private final ExpenseReportArchiveRepository archiveRepository;
        private final ExpenseRepository expenseRepository;
        private final BudgetRepository budgetRepository;
        private final UserService userService; 
        /**
         * Phương thức chính để lấy Báo cáo Chi tiêu.
         * * Logic: So sánh tháng được yêu cầu với tháng hiện tại.
         *   - Nếu là tháng hiện tại: Báo cáo được tạo theo thời gian thực (real-time).
         *   - Nếu là tháng đã qua: Truy xuất báo cáo từ kho lưu trữ (archive).
         * * @param request Yêu cầu báo cáo, chứa tháng và năm cần xem.
         * @return Danh sách các hàng báo cáo (ExpenseReportRow).
         * @throws RuntimeException Nếu báo cáo đã qua nhưng không tìm thấy trong archive.
         */
        public List<ExpenseReportRow> getExpenseReport(ReportRequest request) {
                Long userId = userService.getCurrentUserId();
                int month = Integer.parseInt(request.getMonth());
                int year = Integer.parseInt(request.getYear());
                YearMonth requestYM = YearMonth.of(year, month);
                YearMonth currentYM = YearMonth.now();

                if (requestYM.equals(currentYM)) {
                        // Tháng hiện tại → generate realtime
                        return generateExpenseReportForCurrentUser(userId, request);
                } else {
                        // Tháng khác → lấy từ archive
                        ExpenseReportArchive archive = archiveRepository
                                        .findByUserIdAndMonthAndYear(userId, month, year)
                                        .orElseThrow(() -> new RuntimeException("Report not found in archive"));
                        return archive.getRows();
                }
        }

        /**
         * Tạo Báo cáo Chi tiêu cho người dùng hiện tại trong một tháng/năm cụ thể.
         * * Báo cáo này tổng hợp Chi tiêu (Expense), so sánh với Ngân sách (Budget Limit),
         * và tính toán sự chênh lệch (Difference).
         * * @param userId ID của người dùng cần tạo báo cáo.
         * @param request Yêu cầu báo cáo, chứa tháng và năm.
         * @return Danh sách các hàng báo cáo (ExpenseReportRow) đã được tổng hợp.
         */
        @Override
        public List<ExpenseReportRow> generateExpenseReportForCurrentUser(Long userId, ReportRequest request) {
                // Xử lí tháng người dùng nhập -> startDate và endDate:
                int month = Integer.parseInt(request.getMonth());
                int currentYear = LocalDate.now().getYear();
                YearMonth ym = YearMonth.of(currentYear, month);
                LocalDate startDate = ym.atDay(1);
                LocalDate endDate = ym.atEndOfMonth();

                // 1. Lấy tất cả expense của user trong tháng đó và chỉ lấy loại Chi tiêu
                List<Expense> expenses = expenseRepository
                                .findByUserUserIdAndExpenseDateBetween(userId, startDate, endDate)
                                .stream()
                                .filter(e -> e.getCategory() != null)
                                .filter(e -> "Chi tiêu".equalsIgnoreCase(e.getCategory().getType()))
                                .collect(Collectors.toList());

                // 2. Lấy tất cả budget:
                List<Budget> budgets = budgetRepository
                                .findByUserUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(userId, startDate,
                                                endDate);

                // 3. Map budget theo categoryId (lấy limit đầu tiên nếu trùng) :
                Map<Long, BigDecimal> budgetMap = budgets.stream()
                                .filter(b -> b.getCategory() != null)
                                .filter(b -> "Chi tiêu".equalsIgnoreCase(b.getCategory().getType())) // chỉ budget của
                                                                                                     // chi tiêu
                                .collect(Collectors.toMap(
                                                b -> b.getCategory().getCategoryId(),
                                                b -> b.getAmountLimit() != null ? b.getAmountLimit() : BigDecimal.ZERO,
                                                (existing, replacement) -> existing));

                // 4. Tính tổng chi theo category:
                Map<Long, BigDecimal> expenseSumMap = new HashMap<>();
                for (Expense e : expenses) {
                        Long catId = e.getCategory().getCategoryId();
                        BigDecimal amount = e.getAmount() != null ? e.getAmount() : BigDecimal.ZERO;
                        expenseSumMap.merge(catId, amount, BigDecimal::add);
                }

                // Tổng chi tất cả loại category trong tháng:
                BigDecimal totalSpent = expenseSumMap.values().stream()
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Tổng limit tất cả (cộng limit của các budget) trong tháng:
                BigDecimal totalLimit = budgetMap.values().stream()
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                List<ExpenseReportRow> report = new ArrayList<>();

                // 5. Dòng tổng (dòng đầu tiên):
                report.add(new ExpenseReportRow(
                                "Tổng",
                                totalSpent,
                                totalLimit,
                                totalLimit.subtract(totalSpent)));

                // 5. Gộp tất cả categoryId từ budget và expense 
                Set<Long> allCategoryIds = new HashSet<>();
                allCategoryIds.addAll(budgetMap.keySet()); // 
                allCategoryIds.addAll(expenseSumMap.keySet()); 

                // 6. Duyệt tất cả category:
                for (Long catId : allCategoryIds) {
                        // Tên category ưu tiên từ budget, nếu không có thì lấy từ expense
                        String catName = budgets.stream()
                                        .filter(b -> b.getCategory() != null
                                                        && b.getCategory().getCategoryId().equals(catId))
                                        .map(b -> b.getCategory().getName())
                                        .findFirst()
                                        .orElse(
                                                        expenses.stream()
                                                                        .filter(e -> e.getCategory() != null
                                                                                        && e.getCategory()
                                                                                                        .getCategoryId()
                                                                                                        .equals(catId))
                                                                        .map(e -> e.getCategory().getName())
                                                                        .findFirst()
                                                                        .orElse("Unknown"));

                        BigDecimal spent = expenseSumMap.getOrDefault(catId, BigDecimal.ZERO);
                        BigDecimal limit = budgetMap.getOrDefault(catId, BigDecimal.ZERO);
                        BigDecimal diff = limit.subtract(spent);

                        report.add(new ExpenseReportRow(catName, spent, limit, diff));
                }

                return report;
        }
}
