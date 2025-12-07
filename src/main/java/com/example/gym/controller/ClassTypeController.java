package com.example.gym.controller;

import com.example.gym.entity.ClassType;
import com.example.gym.repository.ClassTypeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClassTypeController {

    private final ClassTypeRepository classTypeRepository;

    public ClassTypeController(ClassTypeRepository classTypeRepository) {
        this.classTypeRepository = classTypeRepository;
    }

    @GetMapping("/api/class-types")   // БЕЗ @ внутри строки
    public List<ClassType> getAll() {
        return classTypeRepository.findAll();
    }
}
