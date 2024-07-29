package com.example.alcohol_free_day.domain.memory.repository;

import com.example.alcohol_free_day.domain.memory.entity.Memory;
import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findAllByUser(User user);

    Memory findTopByUserOrderByCreatedAtDesc(User user);
}
