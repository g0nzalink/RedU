package com.example.backendredu.alumno.application;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.alumno.domain.AlumnoService;
import com.example.backendredu.alumno.domain.Carrera;
import com.example.backendredu.alumno.dto.AlumnoRequestDto;
import com.example.backendredu.alumno.dto.AlumnoResponseDto;
import com.example.backendredu.auth.JwtService;
import com.example.backendredu.usuario.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@WebMvcTest(controllers = {AlumnoController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AlumnoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumnoService alumnoService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    private Alumno alumno;
    private AlumnoResponseDto alumnoResponseDto;

    @BeforeEach
    void setUp() {
        alumno = new Alumno();
        alumno.setEmail("micky.mouse@utec.edu.pe");
        alumno.setPassword("lS9vw76");
        alumno.setUsername("MickyMouse");
        alumno.setCarrera(Carrera.INGENIERIA_ELECTRONICA);

        alumnoResponseDto = new AlumnoResponseDto(
                "micky.mouse@utec.edu.pe",
                "MickyMouse",
                "Hola, soy un rata muy amigable",
                Role.ALUMNO,
                Carrera.INGENIERIA_ELECTRONICA);
    }

    @Test
    void getAlumno_shouldReturnAlumnoResponseDto() throws Exception {
        Mockito.when(alumnoService.getAlumnoById(alumno.getEmail())).thenReturn(alumnoResponseDto);

        mockMvc.perform(get("/alumno/{email}", alumno.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(alumno.getEmail()))
                .andExpect(jsonPath("$.username").value(alumno.getUsername()))
                .andExpect(jsonPath("$.description").value("Hola, soy un rata muy amigable"))
                .andExpect(jsonPath("$.carrera").value(alumno.getCarrera().name()))
                .andExpect(jsonPath("$.userType").value("ALUMNO"));
    }

    @Test
    void register_shouldReturnCreated() throws Exception {
        AlumnoRequestDto request = new AlumnoRequestDto();
        request.setEmail(alumno.getEmail());
        request.setUsername(alumno.getUsername());
        request.setPassword(alumno.getPassword());
        request.setDescription(alumno.getDescription());
        request.setCarrera(alumno.getCarrera());

        mockMvc.perform(post("/alumno/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuario registrado"));

        Mockito.verify(alumnoService).register(
                eq(alumno.getEmail()),
                eq(alumno.getUsername()),
                eq(alumno.getPassword()),
                eq(alumno.getCarrera())
        );
    }

}
