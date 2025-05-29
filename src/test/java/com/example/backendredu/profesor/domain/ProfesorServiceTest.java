package com.example.backendredu.profesor.domain;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.alumno.domain.AlumnoService;
import com.example.backendredu.alumno.domain.Carrera;
import com.example.backendredu.alumno.dto.AlumnoResponseDto;
import com.example.backendredu.alumno.infrastructure.AlumnoRepository;
import com.example.backendredu.exceptions.EmailAlreadyExistsException;
import com.example.backendredu.exceptions.UsernameAlreadyExistsException;
import com.example.backendredu.profesor.dto.ProfesorResponseDto;
import com.example.backendredu.profesor.infrastructure.ProfesorRepository;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfesorServiceTest {
    private ProfesorRepository profesorRepository;
    private ProfesorService profesorService;
    private UsuarioRepository usuarioRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        profesorRepository = mock(ProfesorRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        modelMapper = new ModelMapper();

        profesorService = new ProfesorService(profesorRepository, modelMapper, usuarioRepository);
    }

    @Test
    void getProfesor_shouldReturnMappedProfesor_whenProfesorExist() {
        Profesor profesor = new Profesor();
        profesor.setEmail("goofy@utec.edu.pe");
        profesor.setPassword("lS9vw76");
        profesor.setUsername("Goofy");
        profesor.setDescription("Hola, soy un perro");
        profesor.setDepartamento(Departamento.CIENCIAS);

        Mockito.when(profesorRepository.findById("goofy@utec.edu.pe"))
                .thenReturn(Optional.of(profesor));

        ProfesorResponseDto result = profesorService.getProfesorById("goofy@utec.edu.pe");

        assertThat(result)
                .isNotNull()
                .extracting("email", "username", "description", "departamento")
                .contains("goofy@utec.edu.pe", "Goofy", "Hola, soy un perro", Departamento.CIENCIAS)
        ;
    }

    @Test
    void getProfesor_shouldThrowException_whenProfesorDoesNotExist() {
        String emailInexistente = "micky.mouse@utec.edu.pe";
        when(profesorRepository.findById(emailInexistente))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> profesorService.getProfesorById(emailInexistente))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No existe un profesor con el correo");
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        String email = "goofy@utec.edu.pe";
        String password = "lS9vw76";
        String username = "Goofy";
        Departamento departamento = Departamento.CIENCIAS;

        Mockito.when(usuarioRepository.existsById(email)).thenReturn(true);

        assertThatThrownBy(() ->
                profesorService.register(email, username, password, departamento)
        ).isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email ya registrado");

        Mockito.verify(profesorRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {
        String email = "goofy@utec.edu.pe";
        String password = "lS9vw76";
        String username = "Goofy";
        Departamento departamento = Departamento.CIENCIAS;

        Mockito.when(usuarioRepository.existsByUsername(username)).thenReturn(true);

        assertThatThrownBy(() ->
                profesorService.register(email, username, password, departamento)
        ).isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessage("Nombre de usuario ya registrado");

        Mockito.verify(profesorRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    void register_shouldSaveProfesor() {
        String email = "goofy@utec.edu.pe";
        String password = "lS9vw76";
        String username = "Goofy";
        Departamento departamento = Departamento.CIENCIAS;

        Mockito.when(usuarioRepository.existsById(email)).thenReturn(false);
        Mockito.when(usuarioRepository.existsByUsername(username)).thenReturn(false);

        profesorService.register(email, username, password, departamento);

        Mockito.verify(profesorRepository).save(Mockito.argThat(profesor ->
                profesor.getEmail().equals(email) &&
                        profesor.getUsername().equals(username) &&
                        profesor.getPassword().equals(password) &&
                        profesor.getDepartamento() == departamento
        ));
    }

    @Test
    void register_shouldAssignRoleProfesor() {
        String email = "goofy@utec.edu.pe";
        String password = "lS9vw76";
        String username = "Goofy";
        Departamento departamento = Departamento.CIENCIAS;

        Mockito.when(usuarioRepository.existsById(email)).thenReturn(false);
        Mockito.when(usuarioRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        profesorService.register(email, username, password, departamento);

        Mockito.verify(profesorRepository).save(Mockito.argThat(profesor ->
                profesor.getUserType() == Role.PROFESOR
        ));
    }
}
