package com.example.alcohol_free_day.domain.user.entity;

import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.memory.entity.Memory;
import com.example.alcohol_free_day.domain.report.entity.Report;
import com.example.alcohol_free_day.domain.user.entity.enums.Gender;
import com.example.alcohol_free_day.domain.user.entity.enums.UserStatus;
import com.example.alcohol_free_day.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String password;
    private String nickname;
    private String role;
    private UserStatus status;
    private LocalDate inactiveDate;
    private Gender gender;
    private LocalDate birthDate;
    private Float weight;
    private Float sojuAmount;
    private Long point;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Memory> memoryList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<History> historyList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
