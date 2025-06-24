package com.example.backendredu.profesor.application;

import com.example.backendredu.profesor.domain.ProfesorService;
import com.example.backendredu.profesor.dto.ProfesorRequestDto;
import com.example.backendredu.profesor.dto.ProfesorResponseDto;
import com.example.backendredu.profesor.email.ProfesorEmailEvent;
import com.example.backendredu.profesor.email.ProfesorEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profesor")
public class ProfesorController {

    private final ProfesorService profesorService;
    private final ApplicationEventPublisher applicationEventPublisher;
    
    @GetMapping("/{correo}")
    public ResponseEntity<ProfesorResponseDto> getProfesorById(@PathVariable String correo) {
        ProfesorResponseDto profesor = profesorService.getProfesorById(correo);
        return ResponseEntity.ok(profesor);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ProfesorRequestDto request) {
        profesorService.register(request.getEmail(), request.getUsername(), request.getPassword(),request.getDepartamento(), request.getFotoDePerfil());
        applicationEventPublisher.publishEvent(new ProfesorEmailEvent(new ProfesorEventDto(request.getEmail(), "Hola, profesor(a) " + request.getEmail() + "!", "Se ha logueado correctamente a RedU! \n\n Si no fue usted, por favor responda a este correo con su problema. \n\n Gracias por unirse a RedU!")));
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado");
    }
}

