package com.example.alcohol_free_day.domain.history.repository;

import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, HistoryRepositoryCustom {
    List<History> findByUserAndDateAfter(User user, Date oneWeekAgo);

    List<History> findAllByUser(User user);
}
