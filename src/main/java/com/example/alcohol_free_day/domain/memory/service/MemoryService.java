package com.example.alcohol_free_day.domain.memory.service;

import com.example.alcohol_free_day.domain.memory.converter.MemoryConverter;
import com.example.alcohol_free_day.domain.memory.dto.MemoryRequest;
import com.example.alcohol_free_day.domain.memory.dto.MemoryResponse;
import com.example.alcohol_free_day.domain.memory.entity.Memory;
import com.example.alcohol_free_day.domain.memory.repository.MemoryRepository;
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
public class MemoryService {

    private final MemoryRepository memoryRepository;

    public List<MemoryResponse.PreviewDto> getMemoryPreviewList(User user) {
        List<Memory> memoryList = memoryRepository.findAllByUserOrderByCreatedAtDesc(user);

        return memoryList.stream()
                .map(MemoryConverter::toMemoryPreview).toList();
    }

    public MemoryResponse.DetailDto getMemory(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_MEMORY));

        return MemoryConverter.toMemoryDetail(memory);
    }

    public String documentMemory(User user, MemoryRequest request) {
        Memory memory = Memory.builder()
                .createdAt(request.createdAt())
                .modifiedAt(request.createdAt())
                .doneWhen(request.when())
                .doneWhere(request.where())
                .withWho(request.withWho())
                .what(request.what())
                .how(request.how())
                .why(request.why())
                .content(request.content())
                .user(user)
                .build();

        memoryRepository.save(memory);

        return "작성 완료";
    }

    public String updateMemory(Long memoryId, MemoryRequest request) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_MEMORY));
        memory.update(request);

        memoryRepository.save(memory);

        return "수정 완료";
    }

    public String deleteMemory(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_MEMORY));

        memoryRepository.delete(memory);

        return "삭제 완료";
    }
}
