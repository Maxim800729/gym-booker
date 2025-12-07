package com.example.gym.controller;

import com.example.gym.booking.dto.BookingPaymentUpdateRequest;
import com.example.gym.dto.BookingCreateRequest;
import com.example.gym.dto.BookingResponse;
import com.example.gym.entity.BookingStatus;
import com.example.gym.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping
    public Page<BookingResponse> getAll(
            @RequestParam(defaultValue = "") String q,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return service.getAll(q, pageable);
    }

    @GetMapping("/{id}")
    public BookingResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    // старый простой тумблер оплаты можно оставить
    @PatchMapping("/{id}/paid")
    public BookingResponse setPaid(
            @PathVariable Long id,
            @RequestParam boolean paid
    ) {
        return service.setPaid(id, paid);
    }

    // ✅ НОВОЕ: оплата + оплатить до
    @PatchMapping("/{id}/payment")
    public BookingResponse updatePayment(
            @PathVariable Long id,
            @RequestBody BookingPaymentUpdateRequest req
    ) {
        return service.updatePayment(id, req);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@RequestBody @Valid BookingCreateRequest req) {
        return service.create(req);
    }

    @PatchMapping("/{id}/status/{status}")
    public BookingResponse setStatus(@PathVariable Long id, @PathVariable BookingStatus status) {
        return service.setStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
