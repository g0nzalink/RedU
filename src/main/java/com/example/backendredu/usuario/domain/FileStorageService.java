package com.example.backendredu.usuario.domain;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import org.springframework.core.io.Resource;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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
    private final ClubRepository clubRepository;
    
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
    
    public String storeClubLogo(String email, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Archivo vacío");
        
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (!List.of("jpg", "jpeg", "png").contains(ext.toLowerCase()))
            throw new IllegalArgumentException("Formato no soportado");
        
        Files.createDirectories(rootLocation.resolve("clubs"));
        
        String filename = email + "." + ext;
        Path target = rootLocation.resolve("clubs").resolve(filename);
        
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        
        Club club = clubRepository.findById(email)
                .orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));
        club.setFotoUrl("/clubs/" + filename);
        clubRepository.save(club);
        
        return filename;
    }
    
    public Resource loadClubProfilePhoto(String email) throws MalformedURLException {
        try {
            // Detectar si es .png, .jpg o .jpeg (mejorado)
            Path dir = rootLocation.resolve("clubs");
            String[] extensions = {"png", "jpg", "jpeg"};
            
            for (String ext : extensions) {
                Path file = dir.resolve(email + "." + ext);
                if (Files.exists(file)) {
                    return new UrlResource(file.toUri());
                }
            }
            
            throw new FileNotFoundException("Archivo no encontrado para el email: " + email);
            
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la imagen del club: " + e.getMessage());
        }
    }
    
}

