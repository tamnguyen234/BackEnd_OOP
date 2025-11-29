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

/**
 * Đại diện cho thực thể Lưu trữ Báo cáo Chi tiêu (Expense Report Archive) trong cơ sở dữ liệu.
 * * Lưu trữ kết quả của báo cáo chi tiêu hàng tháng đã được tạo ra,
 * tránh việc phải tính toán lại nhiều lần.
 * * Tương ứng với bảng "expense_report_archive" trong DB.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "expense_report_archive")
public class ExpenseReportArchive {
    /**
     * ID duy nhất của Bản lưu trữ báo cáo (Khóa chính).
     * * Tự động tăng (IDENTITY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * ID của người dùng mà báo cáo này thuộc về.
     * * Đóng vai trò là khóa ngoại logic (không cần ánh xạ ManyToOne vì đây là archive).
     */
    private Long userId;
    /**
     * Tháng mà báo cáo được tạo
     */
    private int month;
    /**
     * Năm mà báo cáo được tạo
     */
    private int year;

    /**
     * Danh sách các dòng dữ liệu (Row) của báo cáo chi tiêu.
     * * @ElementCollection: Dùng để lưu trữ một List các đối tượng có thể nhúng (Embeddable/Value Type),
     * mỗi phần tử được lưu trữ trong một bảng riêng biệt.
     * * @CollectionTable: Định nghĩa tên bảng phụ ("expense_report_rows") sẽ lưu trữ các dòng.
     * * joinColumns: Định nghĩa Khóa ngoại ("archive_id") trong bảng phụ, trỏ ngược về bảng chính này.
     * * Class ExpenseReportRow phải được đánh dấu là @Embeddable (hoặc @Converter tùy thuộc phiên bản JPA).
     */
    @ElementCollection
    @CollectionTable(name = "expense_report_rows", 
            joinColumns = @JoinColumn(name = "archive_id"))
    private List<ExpenseReportRow> rows;
}
