package com.example.gym.controller;

import com.example.gym.dto.ClassSessionCreateRequest;
import com.example.gym.entity.ClassSession;
import com.example.gym.entity.ClassType;
import com.example.gym.entity.Coach;
import com.example.gym.entity.Room;
import com.example.gym.repository.ClassSessionRepository;
import com.example.gym.repository.ClassTypeRepository;
import com.example.gym.repository.CoachRepository;
import com.example.gym.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class ClassSessionController {

    private final ClassSessionRepository classSessionRepository;
    private final ClassTypeRepository classTypeRepository;
    private final RoomRepository roomRepository;
    private final CoachRepository coachRepository;

    @GetMapping
    public List<ClassSession> getAll() {
        return classSessionRepository.findAll();
    }

    @PostMapping
    public ClassSession create(@RequestBody ClassSessionCreateRequest req) {

        ClassType classType = classTypeRepository.findById(req.getClassTypeId())
                .orElseThrow(() -> new IllegalArgumentException("ClassType not found: " + req.getClassTypeId()));

        Room room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + req.getRoomId()));

        Coach coach = coachRepository.findById(req.getCoachId())
                .orElseThrow(() -> new IllegalArgumentException("Coach not found: " + req.getCoachId()));

        ClassSession session = new ClassSession();
        session.setClassType(classType);
        session.setRoom(room);
        session.setCoach(coach);
        session.setStartAt(req.getStartAt());
        session.setCapacity(req.getCapacity());
        session.setCreatedAt(OffsetDateTime.now());

        return classSessionRepository.save(session);
    }
}
