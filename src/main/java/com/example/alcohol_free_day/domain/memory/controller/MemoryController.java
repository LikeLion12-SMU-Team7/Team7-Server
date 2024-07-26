package com.example.alcohol_free_day.domain.memory.controller;

import com.example.alcohol_free_day.domain.memory.dto.MemoryRequest;
import com.example.alcohol_free_day.domain.memory.dto.MemoryResponse;
import com.example.alcohol_free_day.domain.memory.service.MemoryService;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/memory")
public class MemoryController {

    private final MemoryService memoryService;

    @Operation(summary = "흑역사 기록 목록 조회", description = "흑역사 게시글 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<MemoryResponse.Preview>> getMemoryPreviewList(
            @AuthenticationPrincipal User user
            ) {
        return ApiResponse.onSuccess(memoryService.getMemoryPreviewList(user));
    }

    @Operation(summary = "흑역사 상세 기록 조회", description = "흑역사 게시글을 상세 조회합니다.")
    @GetMapping("/{memory_id}")
    public ApiResponse<MemoryResponse.Detail> getMemory(
            @PathVariable(name = "memory_id") Long memoryId
    ) {
        return ApiResponse.onSuccess(memoryService.getMemory(memoryId));
    }

    @Operation(summary = "흑역사 기록", description = "흑역사를 작성합니다.")
    @PostMapping
    public ApiResponse<String> documentMemory (
            @AuthenticationPrincipal User user,
            @RequestBody MemoryRequest request
            ) {
        return ApiResponse.onSuccess(memoryService.documentMemory(user, request));
    }

    @Operation(summary = "흑역사 수정", description = "흑역사를 수정합니다.")
    @PutMapping("/{memory_id}")
    public ApiResponse<String> updateMemory(
            @PathVariable(name = "memory_id") Long memoryId,
            @RequestBody MemoryRequest request
    ) {
        return ApiResponse.onSuccess(memoryService.updateMemory(memoryId, request));
    }

    @Operation(summary = "흑역사 삭제", description = "흑역사를 삭제합니다.")
    @DeleteMapping("/{memory_id}")
    public ApiResponse<String> deleteMemory(
            @PathVariable(name = "memory_id") Long memoryId
    ) {
        return ApiResponse.onSuccess(memoryService.deleteMemory(memoryId));
    }
}
