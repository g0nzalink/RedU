package com.example.backendredu.profesor.domain;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.profesor.dto.ProfesorResponseDto;
import com.example.backendredu.profesor.infrastructure.ProfesorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    private final ModelMapper modelMapper;

    public ProfesorResponseDto getProfesorById (String correo) {
        Profesor profesor = profesorRepository.findById(correo)
                .orElseThrow(() -> new EntityNotFoundException("No existe un alumno con el correo " + correo));
        return modelMapper.map(profesor, ProfesorResponseDto.class);
    }


}

