package com.example.alcohol_free_day.domain.history.repository;

import com.example.alcohol_free_day.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
