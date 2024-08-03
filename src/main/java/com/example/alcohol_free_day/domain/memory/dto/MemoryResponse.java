package com.example.alcohol_free_day.domain.memory.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MemoryResponse() {

    @Builder
    public record Preview(
            Long memoryId,
            LocalDate createdAt,
            String content
    ) {
    }

    @Builder
    public record Detail(
            String when,
            String where,
            String withWho,
            String what,
            String how,
            String why,
            String content,
            LocalDate createdAt
    ) {
    }
}
