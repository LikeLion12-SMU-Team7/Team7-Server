package com.example.alcohol_free_day.domain.report.repository;

import com.example.alcohol_free_day.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
