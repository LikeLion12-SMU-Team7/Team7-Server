package com.example.alcohol_free_day.domain.user.repository;

import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryCustom {
    UserResponse.HomeUserInfoDto findHomeInfo(User user);
}
