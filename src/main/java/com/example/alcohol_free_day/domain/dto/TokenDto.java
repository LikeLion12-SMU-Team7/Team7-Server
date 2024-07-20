package com.example.alcohol_free_day.domain.dto;

import java.util.Date;

public record TokenDto (
        String types,
        String token,
        Date tokenExpiresTime
) {}
