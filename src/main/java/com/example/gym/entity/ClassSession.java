package com.example.gym.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "class_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_type_id", nullable = false)
    private ClassType classType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // >>> НОВОЕ ПОЛЕ ТРЕНЕРА <<<
    @ManyToOne(optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
