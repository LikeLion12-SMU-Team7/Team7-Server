package com.example.alcohol_free_day.domain.history.repository;

import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, HistoryRepositoryCustom {
    List<History> findByUserAndDateAfter(User user, LocalDate oneWeekAgo);

    @Query("SELECT h FROM History h WHERE h.user = :user AND FUNCTION('MONTH', h.date) = :month")
    List<History> findAllByUserAndMonth(User user, Integer month);

    boolean existsByUserAndDate(User user, LocalDate date);

    History findByUserAndDate(User user, LocalDate date);

    List<History> findAllByUser(User user);
}
