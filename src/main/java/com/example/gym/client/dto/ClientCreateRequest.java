package com.example.gym.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientCreateRequest {

    @NotBlank
    private String fullName;

    @NotNull
    private LocalDate birthDate;

    private String address;

    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;
}
