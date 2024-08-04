package com.example.alcohol_free_day.domain.report.dto;

import lombok.Builder;

@Builder
public record ReportResponse(
        String report,
        String status) {
}
