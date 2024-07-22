package com.example.alcohol_free_day.domain.auth.service;

import com.example.alcohol_free_day.domain.auth.dto.AuthRequest;
import com.example.alcohol_free_day.domain.auth.dto.JoinUserRequest;
import com.example.alcohol_free_day.domain.auth.dto.ReissueResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.repository.UserRepository;
import com.example.alcohol_free_day.global.common.code.status.ErrorStatus;
import com.example.alcohol_free_day.global.common.exception.GeneralException;
import com.example.alcohol_free_day.security.jwt.user.CustomUserDetails;
import com.example.alcohol_free_day.security.jwt.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ReissueResponse reissueToken(String refreshToken) {
        log.info("[*] Generate new token pair with " + refreshToken);
        return ReissueResponse.from(jwtUtil.reissueToken(refreshToken));
    }

    public void register(JoinUserRequest request) {
        // 사용자 엔티티 생성
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        // TODO 추가적인 사용자 정보 설정

        userRepository.save(user);
        log.info("[*] User registered with email: " + request.email());
    }

    public ReissueResponse authenticate(AuthRequest request) {
        // 사용자 이메일로 조회
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_USER));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new GeneralException(ErrorStatus._INVALID_USER);
        }

        // JWT 토큰 생성
        String accessToken = jwtUtil.createJwtAccessToken(new CustomUserDetails(user.getUserId(), user.getEmail(), user.getPassword()));
        String refreshToken = jwtUtil.createJwtRefreshToken(new CustomUserDetails(user.getUserId(), user.getEmail(), user.getPassword()));

        log.info("[*] User authenticated: " + request.email());

        return new ReissueResponse(accessToken, refreshToken);
    }
}
