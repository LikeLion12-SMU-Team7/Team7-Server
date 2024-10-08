package com.example.alcohol_free_day.domain.memory.converter;

import com.example.alcohol_free_day.domain.memory.dto.MemoryResponse;
import com.example.alcohol_free_day.domain.memory.entity.Memory;

public class MemoryConverter {

    public static MemoryResponse.PreviewDto toMemoryPreview(Memory memory) {
        return MemoryResponse.PreviewDto.builder()
                .memoryId(memory.getMemoryId())
                .createdAt(memory.getCreatedAt())
                .content(memory.getContent())
                .build();
    }

    public static MemoryResponse.DetailDto toMemoryDetail(Memory memory) {
        return MemoryResponse.DetailDto.builder()
                .when(memory.getDoneWhen())
                .where(memory.getDoneWhere())
                .withWho(memory.getWithWho())
                .what(memory.getWhat())
                .how(memory.getHow())
                .why(memory.getWhy())
                .content(memory.getContent())
                .createdAt(memory.getCreatedAt())
                .build();
    }
}
