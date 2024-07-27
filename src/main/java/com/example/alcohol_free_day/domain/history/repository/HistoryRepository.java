package com.example.alcohol_free_day.domain.history.repository;

import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUserAndDateAfter(User user, Date oneWeekAgo);

    List<History> findAllByUser(User user);
}
