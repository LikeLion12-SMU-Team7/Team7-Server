package com.example.alcohol_free_day.domain.report.repository;

import com.example.alcohol_free_day.domain.report.entity.Report;
import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findTopByUserOrderByCreatedAtDesc(User user);
}
