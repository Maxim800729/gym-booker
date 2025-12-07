package com.example.gym.dto;

import java.time.OffsetDateTime;

public record BookingResponse(
        Long id,
        Long classSessionId,
        String userEmail,
        String status,
        OffsetDateTime createdAt,
        boolean paid,
        OffsetDateTime paidUntil
) {}
