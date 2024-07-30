package com.example.alcohol_free_day.domain.user.repository;

import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);
}
