package com.example.backendredu.profesor.domain;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.profesor.infrastructure.ProfesorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    public Profesor getProfesorById (String correo) {
        return profesorRepository.findById(correo).orElseThrow(() -> new EntityNotFoundException("No existe un alumno con el correo " + correo));
    }


}

