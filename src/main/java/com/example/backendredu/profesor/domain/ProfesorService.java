package com.example.backendredu.profesor.domain;

import com.example.backendredu.exceptions.EmailAlreadyExistsException;
import com.example.backendredu.exceptions.UsernameAlreadyExistsException;
import com.example.backendredu.profesor.dto.ProfesorResponseDto;
import com.example.backendredu.profesor.infrastructure.ProfesorRepository;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    public ProfesorResponseDto getProfesorById (String correo) {
        Profesor profesor = profesorRepository.findById(correo)
                .orElseThrow(() -> new EntityNotFoundException("No existe un profesor con el correo " + correo + "."));
        return modelMapper.map(profesor, ProfesorResponseDto.class);
    }

    public void register(String email, String username, String password, Departamento departamento, String fotoDePerfil) {
        if (usuarioRepository.existsById(email)) {
            throw new EmailAlreadyExistsException("Email ya registrado anteriormente.");
        }

        if (usuarioRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Nombre de usuario ya registrado.");
        }

        Profesor profesor = new Profesor();
        profesor.setEmail(email);
        profesor.setUsername(username);
        profesor.setPassword(encoder.encode(password));
        profesor.setUserType(Role.PROFESOR);
        profesor.setDepartamento(departamento);
        profesor.setFotoPerfilUrl("https://res.cloudinary.com/dvrldorwz/image/upload/v1751791862/redu_padding_imbmse.png");
        profesorRepository.save(profesor);
    }
}

