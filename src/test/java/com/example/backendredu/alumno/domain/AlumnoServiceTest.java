package com.example.backendredu.alumno.domain;

import com.example.backendredu.alumno.dto.AlumnoResponseDto;
import com.example.backendredu.alumno.infrastructure.AlumnoRepository;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import com.example.backendredu.exceptions.*;
import com.example.backendredu.usuario.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AlumnoServiceTest {

    private AlumnoRepository alumnoRepository;
    private AlumnoService alumnoService;
    private UsuarioRepository usuarioRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        alumnoRepository = mock(AlumnoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        modelMapper = new ModelMapper();

        alumnoService = new AlumnoService(alumnoRepository, modelMapper);
        ReflectionTestUtils.setField(alumnoService, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(alumnoService, "encoder", passwordEncoder);
    }

    @Test
    void getAlumno_shouldReturnMappedAlumno_whenAlumnoExist() {
        Alumno alumno = new Alumno();
        alumno.setEmail("micky.mouse@utec.edu.pe");
        alumno.setPassword("lS9vw76");
        alumno.setUsername("MickyMouse");
        alumno.setDescription("Hola, soy una rata muy amigable");
        alumno.setCarrera(Carrera.INGENIERIA_INDUSTRIAL);

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
        String emailInexistente = "goofy@utec.edu.pe";
        when(alumnoRepository.findById(emailInexistente))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> alumnoService.getAlumnoById(emailInexistente))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No existe un alumno con el correo");
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        String email = "micky.mouse@utec.edu.pe";
        String password = "lS9vw76";
        String username = "MickyMouse";
        Carrera carrera = Carrera.INGENIERIA_INDUSTRIAL;

        Mockito.when(usuarioRepository.existsById(email)).thenReturn(true);

        assertThatThrownBy(() ->
                alumnoService.register(email, username, password, carrera)
        ).isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email ya registrado");

        Mockito.verify(alumnoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {
        String email = "micky.mouse@utec.edu.pe";
        String password = "lS9vw76";
        String username = "MickyMouse";
        Carrera carrera = Carrera.INGENIERIA_INDUSTRIAL;

        Mockito.when(usuarioRepository.existsByUsername(username)).thenReturn(true);

        assertThatThrownBy(() ->
                alumnoService.register(email, username, password, carrera)
        ).isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessage("Nombre de usuario ya registrado");

        Mockito.verify(alumnoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void register_shouldCallPasswordEncoder() {
        String email = "micky.mouse@utec.edu.pe";
        String password = "lS9vw76";
        String username = "MickyMouse";
        Carrera carrera = Carrera.INGENIERIA_INDUSTRIAL;

        Mockito.when(usuarioRepository.existsById(email)).thenReturn(false);
        Mockito.when(usuarioRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        alumnoService.register(email, username, password, carrera);

        Mockito.verify(passwordEncoder).encode(password);
    }

    @Test
    void register_shouldSaveAlumno() {
        String email = "micky.mouse@utec.edu.pe";
        String password = "lS9vw76";
        String username = "MickyMouse";
        Carrera carrera = Carrera.INGENIERIA_INDUSTRIAL;

        Mockito.when(usuarioRepository.existsById(email)).thenReturn(false);
        Mockito.when(usuarioRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        alumnoService.register(email, username, password, carrera);

        Mockito.verify(alumnoRepository).save(Mockito.argThat(alumno ->
                alumno.getEmail().equals(email) &&
                        alumno.getUsername().equals(username) &&
                        alumno.getPassword().equals("encodedPassword") &&
                        alumno.getCarrera() == carrera
        ));
    }

    @Test
    void register_shouldAssignRoleAlumno() {
        String email = "micky.mouse@utec.edu.pe";
        String password = "lS9vw76";
        String username = "MickyMouse";
        Carrera carrera = Carrera.INGENIERIA_INDUSTRIAL;

        Mockito.when(usuarioRepository.existsById(email)).thenReturn(false);
        Mockito.when(usuarioRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        alumnoService.register(email, username, password, carrera);

        Mockito.verify(alumnoRepository).save(Mockito.argThat(alumno ->
                alumno.getUserType() == Role.ALUMNO
        ));
    }
}
