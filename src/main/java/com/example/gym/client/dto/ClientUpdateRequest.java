package com.example.gym.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientUpdateRequest {

    // все поля опциональные – что пришло, то и обновляем
    private String fullName;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
}
