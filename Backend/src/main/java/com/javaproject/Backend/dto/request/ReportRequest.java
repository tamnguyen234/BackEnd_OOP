package com.javaproject.Backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    @NotBlank(message = "Month is required")
    private String month;
    @NotBlank(message = "Year is required")
    private String year;
}