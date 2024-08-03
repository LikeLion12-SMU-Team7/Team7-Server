package com.example.alcohol_free_day.domain.memory.dto;

import java.time.LocalDate;

public record MemoryRequest(
        LocalDate createdAt,
        String when,
        String where,
        String withWho,
        String what,
        String how,
        String why,
        String content
) {
}
