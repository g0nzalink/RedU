package com.example.backendredu.profesor.domain;

import com.example.backendredu.profesor.dto.ProfesorResponseDto;
import com.example.backendredu.profesor.infrastructure.ProfesorRepository;
import com.example.backendredu.usuario.domain.Role;
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
                .orElseThrow(() -> new EntityNotFoundException("No existe un profesor con el correo " + correo));
        return modelMapper.map(profesor, ProfesorResponseDto.class);
    }

    public void register(String email, String username, String password) {
        Profesor profesor = new Profesor();
        profesor.setEmail(email);
        profesor.setUsername(username);
        profesor.setUserType(Role.PROFESOR);
        profesorRepository.save(profesor);
    }
}

