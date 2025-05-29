package com.example.backendredu.profesor.application;

import com.example.backendredu.auth.JwtService;
import com.example.backendredu.exceptions.EmailAlreadyExistsException;
import com.example.backendredu.exceptions.UsernameAlreadyExistsException;
import com.example.backendredu.profesor.domain.Departamento;
import com.example.backendredu.profesor.domain.Profesor;
import com.example.backendredu.profesor.domain.ProfesorService;
import com.example.backendredu.profesor.dto.ProfesorRequestDto;
import com.example.backendredu.profesor.dto.ProfesorResponseDto;
import com.example.backendredu.usuario.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ProfesorController.class})
@AutoConfigureMockMvc(addFilters = false)
public class ProfesorControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesorService profesorService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    private Profesor profesor;
    private ProfesorResponseDto profesorResponseDto;

    @BeforeEach
    void setUp() {
        profesor = new Profesor();
        profesor.setEmail("goofy@utec.edu.pe");
        profesor.setPassword("lS9vw76");
        profesor.setUsername("Goofy");
        profesor.setDepartamento(Departamento.CIENCIAS);

        profesorResponseDto = new ProfesorResponseDto(
                "goofy@utec.edu.pe",
                "Goofy",
                "Hola, soy un perro",
                Role.PROFESOR,
                Departamento.CIENCIAS);
    }

    @Test
    void getProfesor_shouldReturnProfesorResponseDto() throws Exception {
        Mockito.when(profesorService.getProfesorById(profesor.getEmail())).thenReturn(profesorResponseDto);

        mockMvc.perform(get("/profesor/{correo}", profesor.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(profesor.getEmail()))
                .andExpect(jsonPath("$.username").value(profesor.getUsername()))
                .andExpect(jsonPath("$.description").value("Hola, soy un perro"))
                .andExpect(jsonPath("$.departamento").value(profesor.getDepartamento().name()))
                .andExpect(jsonPath("$.userType").value("PROFESOR"));
    }

    @Test
    void register_shouldReturnCreated() throws Exception {
        ProfesorRequestDto request = new ProfesorRequestDto();
        request.setEmail(profesor.getEmail());
        request.setUsername(profesor.getUsername());
        request.setPassword(profesor.getPassword());
        request.setDescription(profesor.getDescription());
        request.setDepartamento(profesor.getDepartamento());

        mockMvc.perform(post("/profesor/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuario registrado"));

        Mockito.verify(profesorService).register(
                eq(profesor.getEmail()),
                eq(profesor.getUsername()),
                eq(profesor.getPassword()),
                eq(profesor.getDepartamento())
        );
    }

    @Test
    void getProfesor_shouldReturnNotFoundWhenProfesorDoesNotExist() throws Exception {
        String email = "goofy@utec.edu.pe";
        when(profesorService.getProfesorById(email)).thenThrow(new EntityNotFoundException("Profesor no encontrado"));

        mockMvc.perform(get("/profesor/{email}", email))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Profesor no encontrado"));
    }

    @Test
    void register_shouldReturnConflictWhenEmailExists() throws Exception {
        ProfesorRequestDto request = new ProfesorRequestDto();
        request.setEmail(profesor.getEmail());
        request.setUsername(profesor.getUsername());
        request.setPassword(profesor.getPassword());
        request.setDepartamento(profesor.getDepartamento());

        doThrow(new EmailAlreadyExistsException("Email ya registrado"))
                .when(profesorService).register(anyString(), anyString(), anyString(), any());

        mockMvc.perform(post("/profesor/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email ya registrado"));
    }

    @Test
    void register_shouldReturnConflictWhenUsernameExists() throws Exception {
        ProfesorRequestDto request = new ProfesorRequestDto();
        request.setEmail(profesor.getEmail());
        request.setUsername(profesor.getUsername());
        request.setPassword(profesor.getPassword());
        request.setDescription(profesor.getDescription());
        request.setDepartamento(profesor.getDepartamento());

        doThrow(new UsernameAlreadyExistsException("Nombre de usuario ya registrado"))
                .when(profesorService).register(anyString(), anyString(), anyString(), any());

        mockMvc.perform(post("/profesor/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Nombre de usuario ya registrado"));
    }
}
