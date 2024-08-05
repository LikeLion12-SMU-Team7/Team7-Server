package com.example.alcohol_free_day.domain.report.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReportResponse(
        String report,
        String status) {

    @Builder
    public record PreviewDto (
            Long reportId,
            LocalDateTime createdAt,
            String content
    ) {}

    @Builder
    public record DetailDto (
            LocalDateTime createdAt,
            String content
    ) {}
}
