package com.javaproject.Backend.domain;

import java.util.List;

import com.javaproject.Backend.dto.response.ReportReponse.ExpenseReportRow;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "expense_report_archive")
public class ExpenseReportArchive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private int month;
    private int year;

    // Sử dụng @ElementCollection để Hibernate biết đây là list của Embeddable
    @ElementCollection
    @CollectionTable(name = "expense_report_rows", // tên bảng phụ để lưu các row
            joinColumns = @JoinColumn(name = "archive_id"))
    private List<ExpenseReportRow> rows;
}
