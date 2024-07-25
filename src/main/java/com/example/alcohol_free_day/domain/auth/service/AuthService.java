package com.example.alcohol_free_day.domain.auth.service;

import com.example.alcohol_free_day.domain.auth.dto.AuthRequest;
import com.example.alcohol_free_day.domain.auth.dto.AuthResponse;
import com.example.alcohol_free_day.domain.auth.dto.JoinUserRequest;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.entity.enums.Gender;
import com.example.alcohol_free_day.domain.user.repository.UserRepository;
import com.example.alcohol_free_day.global.common.code.status.ErrorStatus;
import com.example.alcohol_free_day.global.common.exception.GeneralException;
import com.example.alcohol_free_day.security.service.JwtService;
import com.example.alcohol_free_day.security.token.Token;
import com.example.alcohol_free_day.security.token.TokenBlackListRepository;
import com.example.alcohol_free_day.security.token.TokenRepository;
import com.example.alcohol_free_day.security.token.enums.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenBlackListRepository tokenBlackListRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(JoinUserRequest request) {
        var user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .gender(Gender.valueOf(request.gender()))
                .birthDate(request.birthDate())
                .weight(request.weight())
                .sojuAmount(request.sojuAmount())
                .point(10L)
                .build();
        userRepository.save(user);
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException e) {
            throw new GeneralException(ErrorStatus._INVALID_USER);
        }
        var user = userRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        if (tokenBlackListRepository.existsByToken(refreshToken)) {
            throw new NoSuchElementException("유효하지 않은 접근입니다.");
        }
        userEmail = jwtService.extractUserName(refreshToken);
        if (userEmail != null) {
            var usersOptional = this.userRepository.findByEmail(userEmail);
            if (usersOptional.isPresent()) {
                var user = usersOptional.get();
                // 사용자가 존재하는 경우에 대한 처리
                if (jwtService.isTokenValid(refreshToken, user)) {
                    var accessToken = jwtService.generateToken(user);
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);
                    var authResponse = AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                }
            } else {
                // 사용자가 존재하지 않는 경우에 대한 처리
                throw new NoSuchElementException("User not found for email: " + userEmail);
            }
        }
    }
}
