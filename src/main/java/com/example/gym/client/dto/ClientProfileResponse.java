package com.example.gym.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ClientProfileResponse {

    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;

    private List<ClientProfileBookingDto> bookings;
}
