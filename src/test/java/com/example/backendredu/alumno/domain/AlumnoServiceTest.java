package com.example.backendredu.alumno.domain;

import com.example.backendredu.alumno.dto.AlumnoResponseDto;
import com.example.backendredu.alumno.exceptions.AlumnoNotFoundException;
import com.example.backendredu.alumno.infrastructure.AlumnoRepository;
//  import com.example.backendredu.alumno.infrastructure.AlumnoRepositoryTest;
import com.example.backendredu.usuario.domain.Role;
import jakarta.persistence.EntityNotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AlumnoServiceTest {

    private AlumnoRepository alumnoRepository;
    private AlumnoService alumnoService;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        alumnoRepository = mock(AlumnoRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        modelMapper = new ModelMapper();
        alumnoService = new AlumnoService(alumnoRepository, modelMapper);
    }
    /*
        alumnoRequestDto = new AlumnoRequestDto();
        alumnoRequestDto.setPassword("lS9vw76");
        alumnoRequestDto.setUsername("MickeyMouse");
        alumnoRequestDto.setDescription("Hola, soy una rata");
        alumnoRequestDto.setCarrera(Carrera.INGENIERIA_INDUSTRIAL);
     */
    @Test
    void getAlumno_shouldReturnMappedAlumno_whenAlumnoExist() {
        Alumno alumno = new Alumno();
        alumno.setEmail("micky.mouse@utec.edu.pe");
        alumno.setPassword("lS9vw76");
        alumno.setUsername("MickyMouse");
        alumno.setDescription("Hola, soy una rata muy amigable");
        alumno.setCarrera(Carrera.INGENIERIA_INDUSTRIAL);
        alumnoRepository.save(alumno);

        Mockito.when(alumnoRepository.findById("micky.mouse@utec.edu.pe"))
                .thenReturn(Optional.of(alumno));

        AlumnoResponseDto result = alumnoService.getAlumnoById("micky.mouse@utec.edu.pe");

        assertThat(result)
                .isNotNull()
                .extracting("email", "username", "description", "carrera")
                .contains("micky.mouse@utec.edu.pe", "MickyMouse", "Hola, soy una rata muy amigable", Carrera.INGENIERIA_INDUSTRIAL )
        ;
    }

    @Test
    void getAlumno_shouldThrowException_whenAlumnoDoesNotExist() {
        Mockito.when(alumnoRepository.findById("goofy@utec.edu.pe"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> alumnoService.getAlumnoById("goofy@utec.edu.pe"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No existe un alumno con el correo");
    }

    // Todo
    // 1. Se llama al encoder correctamente
    // 2. Se guarda el alumno correctamente
    // 2.1 El email ya existe
    // 2.2 El usuario ya existe
    // 3. El rol se asigna correctamente

}
