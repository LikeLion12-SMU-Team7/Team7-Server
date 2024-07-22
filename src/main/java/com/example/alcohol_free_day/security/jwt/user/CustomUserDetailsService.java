package com.example.alcohol_free_day.security.jwt.user;

import com.example.alcohol_free_day.domain.entity.User;
import com.example.alcohol_free_day.domain.repository.UserRepository;
import com.example.alcohol_free_day.global.common.code.status.ErrorStatus;
import com.example.alcohol_free_day.global.common.exception.GeneralException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TOKEN_ORGANIZATION_NOT_FOND));

        log.info("[*] User found : " + findUser.getEmail());

        return new CustomUserDetails(findUser.getUserId(), email, findUser.getPassword());
    }
}
