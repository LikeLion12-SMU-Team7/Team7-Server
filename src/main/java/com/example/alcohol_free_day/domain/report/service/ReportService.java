package com.example.alcohol_free_day.domain.report.service;

import com.example.alcohol_free_day.domain.report.converter.ReportConverter;
import com.example.alcohol_free_day.domain.report.dto.ReportResponse;
import com.example.alcohol_free_day.domain.report.entity.Report;
import com.example.alcohol_free_day.domain.report.repository.ReportRepository;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.code.status.ErrorStatus;
import com.example.alcohol_free_day.global.common.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public List<ReportResponse.PreviewDto> getReportList(User user) {
        List<Report> reportList = reportRepository.findAllByUserOrderByCreatedAtDesc(user);

        return reportList.stream()
                .map(ReportConverter::toPreview).toList();
    }

    public ReportResponse.DetailDto getReportDetail(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_REPORT));

        return ReportResponse.DetailDto.builder()
                .createdAt(report.getCreatedAt())
                .content(report.getContent())
                .build();
    }

    public ReportResponse.DetailDto getRecentReport(User user) {
        Report report = reportRepository.findTopByUserOrderByCreatedAtDesc(user);

        if(report == null) {
            return ReportResponse.DetailDto.builder()
                    .content(null)
                    .createdAt(null)
                    .build();
        }

        return ReportResponse.DetailDto.builder()
                .content(report.getContent())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
