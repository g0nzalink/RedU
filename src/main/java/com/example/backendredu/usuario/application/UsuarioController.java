package com.example.backendredu.usuario.application;

import com.example.backendredu.usuario.domain.FileStorageService;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.domain.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final FileStorageService storageService;
    private final UsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.allUsuarios());
    }

    @PostMapping("/{email}/profile-photo")
    public ResponseEntity<String> uploadProfilePhoto(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) {
        try {
            storageService.storeProfilePhoto(userDetails.getUsername(), file);
            return ResponseEntity.ok("Foto subida correctamente");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al subir foto: " + e.getMessage());
        }
    }


}

