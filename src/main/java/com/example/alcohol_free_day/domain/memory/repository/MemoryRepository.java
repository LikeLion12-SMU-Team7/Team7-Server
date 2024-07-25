package com.example.alcohol_free_day.domain.memory.repository;

import com.example.alcohol_free_day.domain.memory.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
}
