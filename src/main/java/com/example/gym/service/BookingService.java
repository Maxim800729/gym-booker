package com.example.gym.service;

import com.example.gym.booking.dto.BookingPaymentUpdateRequest;
import com.example.gym.dto.BookingCreateRequest;
import com.example.gym.dto.BookingResponse;
import com.example.gym.entity.Booking;
import com.example.gym.entity.BookingStatus;
import com.example.gym.entity.ClassSession;
import com.example.gym.repository.BookingRepository;
import com.example.gym.repository.ClassSessionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookings;
    private final ClassSessionRepository sessions;

    public BookingService(BookingRepository bookings, ClassSessionRepository sessions) {
        this.bookings = bookings;
        this.sessions = sessions;
    }

    @Transactional(readOnly = true)
    public Page<BookingResponse> getAll(String q, Pageable pageable) {
        return bookings.search(q, pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public BookingResponse get(Long id) {
        return bookings.findById(id)
                .map(this::toDto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Бронь не найдена"));
    }

    // ✅ НОВОЕ: обновление оплаты и срока оплаты
    public BookingResponse updatePayment(Long bookingId, BookingPaymentUpdateRequest req) {

        Booking b = bookings.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Бронь не найдена: " + bookingId
                ));

        if (req.getPaid() != null) {
            b.setPaid(req.getPaid());
            if (!req.getPaid()) {
                b.setPaidUntil(null);
            }
        }

        if (req.getPaidUntil() != null) {
            b.setPaidUntil(req.getPaidUntil());
        }

        return toDto(bookings.save(b));
    }

    public BookingResponse create(BookingCreateRequest req) {

        ClassSession s = sessions.findByIdForUpdate(req.classSessionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Сессия не найдена"));

        boolean existsActive = bookings.existsBySession_IdAndUserEmailAndStatus(
                s.getId(), req.userEmail(), BookingStatus.ACTIVE);
        if (existsActive) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Вы уже записаны на это занятие");
        }

        long activeCount = bookings.countBySession_IdAndStatus(
                s.getId(), BookingStatus.ACTIVE);
        if (activeCount >= s.getCapacity()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Нет свободных мест на это занятие");
        }

        Booking b = new Booking();
        b.setSession(s);
        b.setUserEmail(req.userEmail());
        b.setStatus(BookingStatus.ACTIVE);
        b.setCreatedAt(OffsetDateTime.now());

        // оплата по умолчанию
        b.setPaid(false);

        // ⭐ опционально: можно сразу ставить дедлайн на оплату (например 24 часа)
        // b.setPaidUntil(OffsetDateTime.now().plusHours(24));

        return toDto(bookings.save(b));
    }

    public BookingResponse setStatus(Long id, BookingStatus status) {
        Booking b = bookings.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Бронь не найдена"));
        b.setStatus(status);
        return toDto(bookings.save(b));
    }

    // старый способ оставить можно — не мешает
    public BookingResponse setPaid(Long id, boolean paid) {
        Booking b = bookings.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Бронь не найдена"));

        b.setPaid(paid);
        if (!paid) {
            b.setPaidUntil(null);
        }
        return toDto(bookings.save(b));
    }

    public void delete(Long id) {
        if (!bookings.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Бронь не найдена");
        }
        bookings.deleteById(id);
    }

    private BookingResponse toDto(Booking b) {
        Long sessionId = (b.getSession() != null ? b.getSession().getId() : null);

        // ⚠️ Если ты расширишь BookingResponse и добавишь туда paidUntil,
        // то добавь параметр ниже.
        return new BookingResponse(
                b.getId(),
                sessionId,
                b.getUserEmail(),
                b.getStatus().name(),
                b.getCreatedAt(),
                b.isPaid(),
                b.getPaidUntil()

        );
    }
}
