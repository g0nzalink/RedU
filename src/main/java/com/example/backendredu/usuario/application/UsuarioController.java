package com.example.backendredu.usuario.application;

import com.example.backendredu.cloudinary.CloudinaryService;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final CloudinaryService cloudinaryService;
    private final UsuarioRepository usuarioRepository;
    
    @PostMapping("/{email}/profile-photo")
    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'DIRECTIVA', 'ADMINISTRADOR')")
    public ResponseEntity<String> uploadProfilePhoto(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (!email.equals(userDetails.getUsername())) { return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes cambiar la foto de otro usuario."); }
            String url = cloudinaryService.uploadImage(file, "usuarios", email);
            Usuario usuario = usuarioRepository.findById(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Nombre de usuario no encontrado."));
            usuario.setFotoPerfilUrl(url);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir imagen de perfil: " + e.getMessage());
        }
    }
}
