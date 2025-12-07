package com.example.gym.client.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // относительная папка рядом с jar (можно заменить на абсолютный путь)
    @Value("${app.upload.avatars-dir:uploads/avatars}")
    private String avatarsDir;

    /** Сохраняем аватар и возвращаем веб-URL, который отдаст статик-хэндлер */
    public String saveAvatar(MultipartFile file) throws IOException {
        Path root = Paths.get(avatarsDir);
        Files.createDirectories(root);

        String ext  = getExt(file.getOriginalFilename());
        String name = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);

        Path dst = root.resolve(name);
        Files.copy(file.getInputStream(), dst, StandardCopyOption.REPLACE_EXISTING);

        // Веб-URL, который будет доступен через /uploads/**
        return "/uploads/avatars/" + name;
    }

    private static String getExt(String fn) {
        if (fn == null) return "";
        int dot = fn.lastIndexOf('.');
        return dot >= 0 ? fn.substring(dot + 1) : "";
    }
}
