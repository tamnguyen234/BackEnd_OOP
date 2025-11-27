package com.javaproject.Backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaproject.Backend.domain.ExpenseReportArchive;

import jakarta.transaction.Transactional;

public interface ExpenseReportArchiveRepository
        extends JpaRepository<ExpenseReportArchive, Long> {

    // LẤY RA ARCHIVE CỦA USER + MONTH + YEAR
    Optional<ExpenseReportArchive> findByUserIdAndMonthAndYear(
            Long userId, int month, int year);

    // Xóa những archive cũ hơn 1 tháng nào đó
    @Modifying
    @Transactional
    @Query("DELETE FROM ExpenseReportArchive e " +
            "WHERE e.userId = :userId " +
            "AND (e.year < :minYear OR (e.year = :minYear AND e.month < :minMonth))")
    void deleteOlderThan(
            @Param("userId") Long userId,
            @Param("minMonth") int minMonth,
            @Param("minYear") int minYear);

}
