package com.example.alcohol_free_day.domain.memory.controller;

import com.example.alcohol_free_day.domain.memory.service.MemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/memory")
public class MemoryController {

    private final MemoryService memoryService;
}
