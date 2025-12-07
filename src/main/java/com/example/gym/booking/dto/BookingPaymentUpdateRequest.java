package com.example.gym.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class BookingPaymentUpdateRequest {
    private Boolean paid;
    private OffsetDateTime paidUntil;
}
