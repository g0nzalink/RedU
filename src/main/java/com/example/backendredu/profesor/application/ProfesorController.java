package com.example.backendredu.profesor.application;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.auth.AuthRequest;
import com.example.backendredu.profesor.domain.Profesor;
import com.example.backendredu.profesor.domain.ProfesorService;
import com.example.backendredu.profesor.dto.ProfesorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profesor")
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping("/{correo}")
    public ResponseEntity<ProfesorResponseDto> getProfesorById(@PathVariable String correo) {
        ProfesorResponseDto profesor = profesorService.getProfesorById(correo);
        return ResponseEntity.ok(profesor);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        profesorService.register(request.getEmail(), request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Usuario registrado");
    }
}

