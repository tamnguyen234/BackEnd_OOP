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

// Đánh dấu lớp này là Service và sử dụng Dependency Injection thông qua RequiredArgsConstructor
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
        private final ExpenseReportArchiveRepository archiveRepository;
        private final ExpenseRepository expenseRepository;
        private final BudgetRepository budgetRepository;
        private final UserService userService; // getCurrentUserId()
        // List Report: Sum (chi) compare limit:

        public List<ExpenseReportRow> getExpenseReport(ReportRequest request) {
                Long userId = userService.getCurrentUserId();
                int month = Integer.parseInt(request.getMonth());
                int year = Integer.parseInt(request.getYear());
                YearMonth requestYM = YearMonth.of(year, month);
                YearMonth currentYM = YearMonth.now();

                if (requestYM.equals(currentYM)) {
                        // Tháng hiện tại → generate realtime
                        return generateExpenseReportForCurrentUser(request);
                } else {
                        // Tháng khác → lấy từ archive
                        ExpenseReportArchive archive = archiveRepository
                                        .findByUserIdAndMonthAndYear(userId, month, year)
                                        .orElseThrow(() -> new RuntimeException("Report not found in archive"));
                        return archive.getRows();
                }
        }

        @Override
        public List<ExpenseReportRow> generateExpenseReportForCurrentUser(ReportRequest request) {
                Long userId = userService.getCurrentUserId();

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

                // 3. Map budget theo categoryId (lấy limit đầu tiên nếu trùng) (do mình default
                // bugdet r nên có cũng đc k có cũng đc):
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
                                "Total",
                                totalSpent,
                                totalLimit,
                                totalLimit.subtract(totalSpent)));

                // 5. Gộp tất cả categoryId từ budget và expense (3TH: id có value ở cả 2, id
                // chỉ có value ở budgetMap, id chỉ có value ở expenseSumMap):
                Set<Long> allCategoryIds = new HashSet<>();
                allCategoryIds.addAll(budgetMap.keySet()); // add cateID có limit vào
                allCategoryIds.addAll(expenseSumMap.keySet()); // add cateID có expen vào

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

        @Override
        public void scheduledMonthlyReport() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'scheduledMonthlyReport'");
        }

        @Override
        public List<ExpenseReportRow> generateExpenseReport(Long userId, ReportRequest request) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'generateExpenseReport'");
        }

        // // ==== TẠO BÁO CÁO (generateReport) ====
        // @Override // Triển khai phương thức từ interface ReportService.
        // public ReportResponse generateReport(ReportRequest request) {
        // LocalDate start = request.getStartDate();
        // LocalDate end = request.getEndDate();
        // if (start == null || end == null) {
        // // thiết lập mặc định là TỪ ĐẦU THÁNG HIỆN TẠI đến HÔM NAY.
        // LocalDate now = LocalDate.now();
        // start = now.withDayOfMonth(1); // ngày 1 tháng hiện tại
        // end = now;
        // }
        // // Danh sách để lưu trữ các đối tượng Chi tiêu (Expense)
        // List<com.javaproject.Backend.domain.Expense> expenses;
        // if (request.getUserId() != null) {
        // // Báo cáo chi tiêu 1 người dùng nhất định theo UserID
        // expenses =
        // expenseRepository.findByUserUserIdAndExpenseDateBetween(request.getUserId(),
        // start, end);
        // } else {
        // // Báo cáo toàn hệ thống
        // expenses = expenseRepository.findByExpenseDateBetween(start, end);
        // }

        // // Tổng chi tiêu
        // BigDecimal total = expenses.stream()
        // // Lấy trường 'amount' (số tiền) từ mỗi đối tượng Expense
        // .map(com.javaproject.Backend.domain.Expense::getAmount)
        // // Cộng tổng các số tiền lại, bắt đầu từ 0 (BigDecimal.ZERO)
        // .reduce(BigDecimal.ZERO, BigDecimal::add);
        // return ReportResponse.builder()
        // // .userId(request.getUserId())
        // .startDate(start)
        // .endDate(end)
        // .totalExpense(total) // Tổng chi tiêu
        // .build();
        // }

        // // ==== BÁO CÁO ĐỊNH KỲ HÀNG THÁNG ====
        // @Override
        // public void scheduledMonthlyReport() {
        // // Lấy ngày hiện tại
        // LocalDate now = LocalDate.now();
        // // Ngày bắt đầu: Ngày 1 của tháng trước
        // LocalDate start = now.minusMonths(1) // trừ đi 1 tháng so với ngày được gọi
        // .withDayOfMonth(1);// thiết lập này về ngày int nhập vào ở đây là 1. Trả về
        // đối tượng localdate mới
        // // Ngày kết thúc: Ngày cuối cùng của tháng trước
        // LocalDate end = now.minusMonths(1)
        // .withDayOfMonth(now.minusMonths(1).lengthOfMonth()); // trả về số ngày tháng
        // trước
        // ReportRequest r =
        // ReportRequest.builder().startDate(start).endDate(end).build();
        // ReportResponse resp = generateReport(r);
        // // Trong môi trường thực tế:
        // // - Lưu báo cáo vào DB (cho lịch sử).
        // // - Gửi email cho quản lý.
        // // - Cập nhật dashboard.
        // // in ra console để kiểm tra
        // System.out.println("Scheduled monthly report: " + resp.toString());
        // }
}
