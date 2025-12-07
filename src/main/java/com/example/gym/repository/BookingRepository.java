package com.example.gym.repository;

import com.example.gym.entity.Booking;
import com.example.gym.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Проверка: есть ли у пользователя активная бронь на эту сессию
    boolean existsBySession_IdAndUserEmailAndStatus(Long sessionId,
                                                    String userEmail,
                                                    BookingStatus status);

    // Сколько броней с таким статусом на конкретную сессию
    long countBySession_IdAndStatus(Long sessionId, BookingStatus status);

    // Пагинация по email (если где-то используется)
    Page<Booking> findByUserEmail(String userEmail, Pageable pageable);

    // Все брони пользователя, отсортированные по дате создания (для профиля клиента)
    List<Booking> findByUserEmailOrderByCreatedAtDesc(String email);

    // Поиск по email (для админки/списка бронирований, если нужно)
    @Query("""
           select b
           from Booking b
           where (:q is null or :q = ''
                  or lower(b.userEmail) like lower(concat('%', :q, '%')))
           """)
    Page<Booking> search(@Param("q") String q, Pageable pageable);
}
