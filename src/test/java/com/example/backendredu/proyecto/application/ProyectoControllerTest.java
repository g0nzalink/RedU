package com.example.backendredu.proyecto.application;

import com.example.backendredu.proyecto.application.ProyectoController;
import com.example.backendredu.proyecto.domain.ProyectoService;
import com.example.backendredu.proyecto.domain.Status;
import com.example.backendredu.proyecto.dto.ProyectoRequestDto;
import com.example.backendredu.proyecto.dto.ProyectoResponseDto;
import com.example.backendredu.auth.JwtService;
import com.example.backendredu.publicacion.domain.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProyectoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProyectoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProyectoService proyectoService;

    @MockBean
    private JwtService jwtService;

    private ProyectoResponseDto proyectoDto;
    private ProyectoRequestDto requestDto;

    @BeforeEach
    void setUp() {
        proyectoDto = new ProyectoResponseDto();
        proyectoDto.setId(1L);
        proyectoDto.setAutor("estudiante@correo.com");
        proyectoDto.setFechaPublicacion(LocalDateTime.now());
        proyectoDto.setFechaModificacion(LocalDateTime.now());
        proyectoDto.setTitulo("Proyecto de IA");
        proyectoDto.setDescripcion("Sistema de recomendaciones con Machine Learning");
        proyectoDto.setStatus(Status.ACTIVO);
        proyectoDto.setListTag(List.of("INTELIGENCIA_ARTIFICIAL", "PROGRAMACION"));

        requestDto = new ProyectoRequestDto();
        requestDto.setTitulo("Nuevo Proyecto");
        requestDto.setDescripcion("Descripción del nuevo proyecto");
        requestDto.setStatus(Status.ACTIVO);
        requestDto.setListTag(List.of(Tag.CLUB, Tag.CAPACITACION));
    }

    @Test
    void listarProyectos_deberiaRetornarLista() throws Exception {
        when(proyectoService.allProyectos()).thenReturn(List.of(proyectoDto));

        mockMvc.perform(get("/proyecto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Proyecto de IA"))
                .andExpect(jsonPath("$[0].autor").value("estudiante@correo.com")) // Cambio: autorEmail -> autor
                .andExpect(jsonPath("$[0].status").value("ACTIVO")); // Cambio: estado -> status, EN_PROGRESO -> ACTIVO

        verify(proyectoService).allProyectos();
    }

    @Test
    void obtenerProyecto_deberiaRetornarProyectoPorId() throws Exception {
        when(proyectoService.obtenerProyecto(1L)).thenReturn(proyectoDto);

        mockMvc.perform(get("/proyecto/{proyectoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Proyecto de IA"))
                .andExpect(jsonPath("$.descripcion").value("Sistema de recomendaciones con Machine Learning"))
                .andExpect(jsonPath("$.autor").value("estudiante@correo.com")) // Cambio: autorEmail -> autor
                .andExpect(jsonPath("$.status").value("ACTIVO")); // Cambio: estado -> status, EN_PROGRESO -> ACTIVO

        verify(proyectoService).obtenerProyecto(1L);
    }

    @Test
    @WithMockUser(username = "estudiante@correo.com", roles = {"ALUMNO"})
    void crearProyecto_conRolAlumno_deberiaCrearYDevolverProyecto() throws Exception {
        when(proyectoService.crearProyecto(any(ProyectoRequestDto.class), eq("estudiante@correo.com")))
                .thenReturn(proyectoDto);

        mockMvc.perform(post("/proyecto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/proyecto/1"))
                .andExpect(jsonPath("$.titulo").value("Proyecto de IA"))
                .andExpect(jsonPath("$.autor").value("estudiante@correo.com")); // Cambio: autorEmail -> autor

        verify(proyectoService).crearProyecto(any(ProyectoRequestDto.class), eq("estudiante@correo.com"));
    }

    @Test
    @WithMockUser(username = "profesor@correo.com", roles = {"PROFESOR"})
    void crearProyecto_conRolProfesor_deberiaCrearYDevolverProyecto() throws Exception {
        ProyectoResponseDto proyectoProfesor = new ProyectoResponseDto();
        proyectoProfesor.setId(2L);
        proyectoProfesor.setAutor("profesor@correo.com");
        proyectoProfesor.setFechaPublicacion(LocalDateTime.now());
        proyectoProfesor.setFechaModificacion(LocalDateTime.now());
        proyectoProfesor.setTitulo("Proyecto de Investigación");
        proyectoProfesor.setDescripcion("Investigación en algoritmos cuánticos");
        proyectoProfesor.setStatus(Status.ACTIVO);
        proyectoProfesor.setListTag(List.of("INVESTIGACION", "ALGORITMOS"));

        when(proyectoService.crearProyecto(any(ProyectoRequestDto.class), eq("profesor@correo.com")))
                .thenReturn(proyectoProfesor);

        mockMvc.perform(post("/proyecto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/proyecto/2"))
                .andExpect(jsonPath("$.titulo").value("Proyecto de Investigación"))
                .andExpect(jsonPath("$.autor").value("profesor@correo.com")); // Cambio: autorEmail -> autor

        verify(proyectoService).crearProyecto(any(ProyectoRequestDto.class), eq("profesor@correo.com"));
    }

    @Test
    @WithMockUser(username = "estudiante@correo.com", roles = {"ALUMNO"})
    void actualizarProyecto_deberiaActualizarCampos() throws Exception {
        ProyectoRequestDto updateDto = new ProyectoRequestDto();
        updateDto.setTitulo("Proyecto Actualizado");
        updateDto.setDescripcion("Descripción actualizada del proyecto");
        updateDto.setStatus(Status.ACTIVO);
        updateDto.setListTag(List.of(Tag.ARTE));

        ProyectoResponseDto updatedDto = new ProyectoResponseDto();
        updatedDto.setId(1L);
        updatedDto.setAutor("estudiante@correo.com");
        updatedDto.setFechaPublicacion(LocalDateTime.now());
        updatedDto.setFechaModificacion(LocalDateTime.now());
        updatedDto.setTitulo("Proyecto Actualizado");
        updatedDto.setDescripcion("Descripción actualizada del proyecto");
        updatedDto.setStatus(Status.ACTIVO);
        updatedDto.setListTag(List.of("CODING")); // Cambio: lista vacía -> lista con "CODING"

        when(proyectoService.actualizarProyecto(any(ProyectoRequestDto.class), eq(1L), eq("estudiante@correo.com")))
                .thenReturn(updatedDto);

        mockMvc.perform(patch("/proyecto/actualizar/{proyectoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Proyecto Actualizado"))
                .andExpect(jsonPath("$.status").value("ACTIVO")) // Cambio: estado -> status, COMPLETADO -> ACTIVO
                .andExpect(jsonPath("$.descripcion").value("Descripción actualizada del proyecto"));

        verify(proyectoService).actualizarProyecto(any(ProyectoRequestDto.class), eq(1L), eq("estudiante@correo.com"));
    }
}