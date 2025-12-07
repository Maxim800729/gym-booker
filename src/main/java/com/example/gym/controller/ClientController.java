package com.example.gym.controller;

import com.example.gym.client.dto.*;
import com.example.gym.client.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/clients", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    private final ClientService service;

    // Оставляем как есть (может использоваться админом/внутренне)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ClientResponse create(@Valid @RequestBody ClientCreateRequest req) {
        return service.create(req);
    }

    // Профиль + аватар
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ClientResponse update(
            @PathVariable Long id,
            @Valid @ModelAttribute ClientUpdateRequest req,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        return service.update(id, req, avatar);
    }

    @GetMapping("/{id}")
    public ClientResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/profile/by-email")
    public ClientProfileResponse getProfile(@RequestParam String email) {
        return service.getProfileByEmail(email);
    }

    // Регистрация клиента
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse register(@RequestBody @Valid ClientRegisterRequest req) {
        return service.register(req);
    }

    // ✅ Логин клиента (то, что нужно для "входа" в кабинет)
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ClientResponse login(@RequestBody @Valid ClientLoginRequest req) {
        return service.login(req);
    }

    // ✅ Смена пароля клиента
    @PostMapping(value = "/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @PathVariable Long id,
            @RequestBody @Valid ChangePasswordRequest req
    ) {
        service.changePassword(id, req);
    }
}
