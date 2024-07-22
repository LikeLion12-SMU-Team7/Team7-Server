package com.example.alcohol_free_day.security.jwt.exception;

import java.io.IOException;

import com.example.alcohol_free_day.global.common.ApiResponse;
import com.example.alcohol_free_day.global.common.code.status.ErrorStatus;
import com.example.alcohol_free_day.security.jwt.util.HttpResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        HttpStatus httpStatus;
        ApiResponse<String> errorResponse;

        log.error(">>>>>> AuthenticationException: ", authException);
        httpStatus = HttpStatus.UNAUTHORIZED;
        errorResponse = ApiResponse.onFailure(
                ErrorStatus.UNAUTHORIZED.getCode(),
                ErrorStatus.UNAUTHORIZED.getMessage(),
                authException.getMessage());

        HttpResponseUtil.setErrorResponse(response, httpStatus, errorResponse);
    }
}
