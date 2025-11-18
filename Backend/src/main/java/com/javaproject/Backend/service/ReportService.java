package com.javaproject.Backend.service;

import com.javaproject.Backend.dto.request.ReportRequest;
import com.javaproject.Backend.dto.response.ReportResponse;

public interface ReportService {
    ReportResponse generateReport(ReportRequest request);

    void scheduledMonthlyReport(); // called by scheduler
}