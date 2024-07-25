package com.example.alcohol_free_day.domain.memory.service;

import com.example.alcohol_free_day.domain.memory.repository.MemoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemoryService {

    private final MemoryRepository memoryRepository;
}
