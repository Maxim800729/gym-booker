package com.example.gym.client.service;

import com.example.gym.client.dto.*;
import com.example.gym.client.entity.Client;
import com.example.gym.client.repository.ClientRepository;
import com.example.gym.entity.Booking;
import com.example.gym.repository.BookingRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public ClientService(ClientRepository clientRepository,
                         BookingRepository bookingRepository,
                         PasswordEncoder passwordEncoder,
                         FileStorageService fileStorageService) {
        this.clientRepository = clientRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }


    // ===================== CRUD клиента =====================

    @Transactional
    public ClientResponse create(ClientCreateRequest req) {
        Client client = new Client();
        client.setFullName(req.getFullName());
        client.setBirthDate(req.getBirthDate());
        client.setAddress(req.getAddress());
        client.setPhone(req.getPhone());
        client.setEmail(req.getEmail());

        Client saved = clientRepository.save(client);
        return toResponse(saved);
    }

    @Transactional
    public ClientResponse update(Long id,
                                 ClientUpdateRequest req,
                                 MultipartFile avatar) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Клиент с id=" + id + " не найден"));

        if (req.getFullName() != null) client.setFullName(req.getFullName());
        if (req.getBirthDate() != null) client.setBirthDate(req.getBirthDate());
        if (req.getAddress() != null) client.setAddress(req.getAddress());
        if (req.getPhone() != null) client.setPhone(req.getPhone());
        if (req.getEmail() != null) client.setEmail(req.getEmail());

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String url = fileStorageService.saveAvatar(avatar);
                client.setAvatarUrl(url);
            } catch (Exception e) {
                throw new IllegalArgumentException("Не удалось сохранить аватар");
            }
        }

        Client saved = clientRepository.save(client);
        return toResponse(saved);
    }


    @Transactional(readOnly = true)
    public ClientResponse get(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Клиент с id=" + id + " не найден"));
        return toResponse(client);
    }

    private ClientResponse toResponse(Client client) {
        ClientResponse resp = new ClientResponse();
        resp.setId(client.getId());
        resp.setFullName(client.getFullName());
        resp.setBirthDate(client.getBirthDate());
        resp.setAddress(client.getAddress());
        resp.setPhone(client.getPhone());
        resp.setEmail(client.getEmail());
        resp.setAvatarUrl(client.getAvatarUrl());
        resp.setCreatedAt(client.getCreatedAt());
        return resp;
    }

    // ===================== Профиль клиента (личный кабинет) =====================

    @Transactional(readOnly = true)
    public ClientProfileResponse getProfileByEmail(String email) {

        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Клиент с таким email не найден"));

        List<Booking> bookings = bookingRepository
                .findByUserEmailOrderByCreatedAtDesc(email);

        List<ClientProfileBookingDto> bookingDtos = bookings.stream()
                .map(b -> {
                    ClientProfileBookingDto dto = new ClientProfileBookingDto();

                    dto.setBookingId(b.getId());
                    dto.setClassName(b.getSession().getClassType().getTitle());

                    if (b.getSession().getStartAt() != null) {
                        dto.setStartTime(b.getSession().getStartAt().toLocalDateTime());
                    }

                    Integer durationMin =
                            b.getSession().getClassType().getDurationMin();
                    dto.setDurationMinutes(durationMin != null ? durationMin : 0);

                    dto.setRoomName(b.getSession().getRoom().getName());

                    if (b.getSession().getCoach() != null) {
                        dto.setCoachName(b.getSession().getCoach().getFullName());
                    }

                    dto.setStatus(b.getStatus().name());

                    // если поле price в DTO есть — можно заполнить потом
                    // dto.setPrice(null);

                    dto.setPaid(b.isPaid());   // <- один раз, без дублей

                    return dto;
                })
                .collect(Collectors.toList());

        ClientProfileResponse response = new ClientProfileResponse();
        response.setId(client.getId());
        response.setFullName(client.getFullName());
        response.setBirthDate(client.getBirthDate());
        response.setAddress(client.getAddress());
        response.setPhone(client.getPhone());
        response.setEmail(client.getEmail());
        response.setAvatarUrl(client.getAvatarUrl());
        response.setCreatedAt(client.getCreatedAt());
        response.setBookings(bookingDtos);

        return response;
    }

    // ===================== Регистрация =====================

    @Transactional
    public ClientResponse register(ClientRegisterRequest req) {

        if (clientRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Клиент с таким email уже существует");
        }

        Client c = new Client();
        c.setFullName(req.getFullName());
        c.setEmail(req.getEmail());
        c.setPhone(req.getPhone());
        c.setAddress(req.getAddress());
        // если добавишь birthDate в DTO — сюда тоже
        c.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        Client saved = clientRepository.save(c);
        return toResponse(saved);
    }

    // ===================== Логин =====================

    @Transactional(readOnly = true)
    public ClientResponse login(ClientLoginRequest req) {

        Client client = clientRepository.findByEmail(req.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Неверный email или пароль"));

        if (!passwordEncoder.matches(req.getPassword(), client.getPasswordHash())) {
            throw new IllegalArgumentException("Неверный email или пароль");
        }

        return toResponse(client);
    }
    // ===================== Смена пароля =====================

    @Transactional
    public void changePassword(Long clientId, ChangePasswordRequest req) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Клиент с id=" + clientId + " не найден"));

        // проверяем текущий пароль
        if (!passwordEncoder.matches(req.getCurrentPassword(), client.getPasswordHash())) {
            throw new IllegalArgumentException("Текущий пароль указан неверно");
        }

        // проверяем совпадение новых паролей
        if (!req.getNewPassword().equals(req.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Новые пароли не совпадают");
        }

        // кодируем и сохраняем
        client.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        clientRepository.save(client);
    }

}
