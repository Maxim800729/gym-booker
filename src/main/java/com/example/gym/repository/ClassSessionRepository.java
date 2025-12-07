// src/main/java/com/example/gym/repository/ClassSessionRepository.java
package com.example.gym.repository;

import jakarta.persistence.LockModeType;
import com.example.gym.entity.ClassSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {

    // Опционально: блокировка на время транзакции (поможет при высокой конкуренции)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ClassSession s where s.id = :id")
    Optional<ClassSession> findByIdForUpdate(@Param("id") Long id);
}
