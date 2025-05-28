package com.example.backendredu.usuario.application;

import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.domain.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    //endpoint para que el admin tenga control sobre los usuarios registrados
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.allUsuarios());
    }
}

