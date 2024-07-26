package com.example.alcohol_free_day.domain.memory.dto;

public record MemoryRequest(
        String when,
        String where,
        String withWho,
        String what,
        String how,
        String why,
        String content
) {
}
