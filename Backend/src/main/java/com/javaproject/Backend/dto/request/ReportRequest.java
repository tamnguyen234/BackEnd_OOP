package com.javaproject.Backend.dto.request;


import java.time.LocalDate;

import lombok.Data;
// --- ReportRequest ---
@Data
public class ReportRequest {
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
}
