package com.example.gym.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BookingCreateRequest(
        @NotNull Long classSessionId,
        @NotBlank @Email String userEmail
) {}
