package com.example.alcohol_free_day.domain.report.converter;

import com.example.alcohol_free_day.domain.report.dto.ReportResponse;
import com.example.alcohol_free_day.domain.report.entity.Report;

public class ReportConverter {

    public static ReportResponse.PreviewDto toPreview(Report report) {
        return ReportResponse.PreviewDto.builder()
                .reportId(report.getReportId())
                .createdAt(report.getCreatedAt())
                .content(report.getContent())
                .build();
    }
}
