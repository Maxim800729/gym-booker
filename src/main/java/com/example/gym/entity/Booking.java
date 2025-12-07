package com.example.gym.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "bookings",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_booking",
                columnNames = {"user_email", "session_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private ClassSession session;

    @Column(name = "user_email", nullable = false, length = 200)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BookingStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    // 🔹 НОВОЕ ПОЛЕ: оплачена ли бронь
    @Column(name = "paid", nullable = false)
    private boolean paid ;

    // 🔹 НОВОЕ ПОЛЕ: до какого времени можно оплатить
    @Column(name = "paid_until")
    private OffsetDateTime paidUntil;

}
