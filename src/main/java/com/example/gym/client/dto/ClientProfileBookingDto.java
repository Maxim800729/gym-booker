package com.example.gym.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClientProfileBookingDto {

    private Long bookingId;
    private String className;
    private LocalDateTime startTime;
    private int durationMinutes;
    private String roomName;
    private String status;     // BOOKED / CANCELLED / ...
    private Integer price;     // если есть цена
    private boolean paid;      // оплачен ли

    // НОВОЕ поле — имя тренера
    private String coachName;
}
