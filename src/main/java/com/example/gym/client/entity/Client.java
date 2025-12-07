package com.example.gym.client.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private LocalDate birthDate;

    private String address;

    @Column(length = 64)
    private String phone;

    @Column(length = 128)
    private String email;

    // com.example.gym.client.entity.Client

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;


    // URL до аватарки, которую будем отдавать со статического хэндлера
    private String avatarUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
