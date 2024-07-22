package com.example.alcohol_free_day.security.jwt.filter;

import java.util.concurrent.TimeUnit;

import com.example.alcohol_free_day.config.util.RedisUtil;
import com.example.alcohol_free_day.global.common.code.status.ErrorStatus;
import com.example.alcohol_free_day.global.common.exception.GeneralException;
import com.example.alcohol_free_day.security.jwt.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            log.info("[*] Logout Filter");

            String accessToken = jwtUtil.resolveAccessToken(request);

            redisUtil.saveAsValue(
                    accessToken,
                    "logout",
                    jwtUtil.getExpTime(accessToken),
                    TimeUnit.MILLISECONDS
            );

            String email = jwtUtil.getEmail(accessToken);

            redisUtil.delete(
                    email + "_refresh_token"
            );

            redisUtil.delete(
                    email + "_fcm_token"
            );

        } catch (ExpiredJwtException e) {
            log.warn("[*] case : accessToken expired");
            throw new GeneralException(ErrorStatus.TOKEN_EXPIRED);
        }
    }
}
