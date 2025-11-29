package com.javaproject.Backend.scheduler;

import java.time.YearMonth;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.javaproject.Backend.domain.ExpenseReportArchive;
import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportReponse.ExpenseReportRow;
import com.javaproject.Backend.repository.ExpenseReportArchiveRepository;
import com.javaproject.Backend.service.ReportService;
import com.javaproject.Backend.service.UserService;
/**
 * Lập lịch báo cáo hàng tháng
 */
@Component
public class ExpenseReportScheduler {

    private final ReportService reportService;
    private final ExpenseReportArchiveRepository archiveRepository;
    private final UserService userService;

    public ExpenseReportScheduler(
            ReportService reportService,
            ExpenseReportArchiveRepository archiveRepository,
            UserService userService) {
        this.reportService = reportService;
        this.archiveRepository = archiveRepository;
        this.userService = userService;
    }

    /**
     * Chạy 23:59:59 ngày cuối cùng của mỗi tháng
     * Lưu báo cáo tháng hiện tại
     */
    @Scheduled(cron = "59 59 23 L * ?")
    @Transactional
    public void archiveMonthlyReport() {

        // Lấy danh sách tất cả user
        List<Long> userIds = userService.getAllUserIds();

        // Tháng hiện tại
        YearMonth currentMonth = YearMonth.now();

        for (Long userId : userIds) {

            // Tạo request cho tháng hiện tại
            ReportRequest request = new ReportRequest();
            request.setMonth(String.valueOf(currentMonth.getMonthValue()));
            request.setYear(String.valueOf(currentMonth.getYear()));

            // Generate report cho user + tháng hiện tại
            List<ExpenseReportRow> rows = reportService.generateExpenseReportForCurrentUser(userId, request);

            // Tạo archive chứa toàn bộ rows
            ExpenseReportArchive archive = new ExpenseReportArchive();
            archive.setUserId(userId);
            archive.setMonth(currentMonth.getMonthValue());
            archive.setYear(currentMonth.getYear());
            archive.setRows(rows);

            // Lưu vào DB
            archiveRepository.save(archive);

            // Xóa archive cũ quá 3 tháng so với tháng hiện tại:
            YearMonth minMonthToKeep = YearMonth.now().minusMonths(2); // trừ đi 2 tháng (tự động chuyển year)
            archiveRepository.deleteOlderThan(
                    userId,
                    minMonthToKeep.getMonthValue(),
                    minMonthToKeep.getYear());
        }
    }
}
