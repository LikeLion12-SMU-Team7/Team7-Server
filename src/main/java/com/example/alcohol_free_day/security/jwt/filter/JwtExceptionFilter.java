package com.example.alcohol_free_day.security.jwt.filter;

import java.io.IOException;

import com.example.alcohol_free_day.global.common.ApiResponse;
import com.example.alcohol_free_day.global.common.code.status.ErrorStatus;
import com.example.alcohol_free_day.security.jwt.util.HttpResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            log.warn(">>>>> RuntimeException : ", e);
        } catch (Exception e) {
            log.error(">>>>> Exception : ", e);
            ApiResponse<String> errorResponse = ApiResponse.onFailure(
                    ErrorStatus.INTERNAL_SECURITY_ERROR.getCode(),
                    ErrorStatus.INTERNAL_SECURITY_ERROR.getMessage(),
                    e.getMessage()
            );
            HttpResponseUtil.setErrorResponse(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    errorResponse
            );
        }
    }
}
