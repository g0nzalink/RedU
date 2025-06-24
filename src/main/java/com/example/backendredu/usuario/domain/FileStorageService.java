package com.example.backendredu.usuario.domain;

import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final UsuarioRepository usuarioRepository;
    private final Path rootLocation = Paths.get("profile-photos");

    public String storeProfilePhoto(String email, MultipartFile file) throws IOException {
        if (file.isEmpty()) {throw new IllegalArgumentException("Archivo vacío");}

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (!List.of("jpg","jpeg","png").contains(ext.toLowerCase())) {throw new IllegalArgumentException("Formato no soportado");}

        Files.createDirectories(rootLocation);

        String filename = email + "." + ext;
        Path target = rootLocation.resolve(filename);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        Usuario user = usuarioRepository.findById(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        user.setFotoPerfilUrl("/users/" + email + "/profile-photo");
        usuarioRepository.save(user);

        return filename;
    }
}

