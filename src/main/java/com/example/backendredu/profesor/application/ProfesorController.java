package com.example.backendredu.profesor.application;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.profesor.domain.Profesor;
import com.example.backendredu.profesor.domain.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profesor")
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping("/{correo}")
    public ResponseEntity<Profesor> getProfesorById(@PathVariable String correo) {
        Profesor profesor = profesorService.getProfesorById(correo);
        return ResponseEntity.ok(profesor);
    }
}

