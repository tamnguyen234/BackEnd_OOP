package com.javaproject.Backend.service;

import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportResponse;

public interface ReportService {
    // ==== TẠO BÁO CÁO (generateReport) ====
    ReportResponse generateReport(ReportRequest request);
    // ==== BÁO CÁO ĐỊNH KỲ HÀNG THÁNG ====
    void scheduledMonthlyReport(); // gọi bởi scheduler
}