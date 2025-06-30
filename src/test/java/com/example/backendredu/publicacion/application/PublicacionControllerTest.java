package com.example.backendredu.publicacion.application;

import com.example.backendredu.publicacion.application.PublicacionController;
import com.example.backendredu.publicacion.domain.PublicacionService;
import com.example.backendredu.publicacion.domain.Tag;
import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.publicacion.dto.PublicacionUpdateDto;
import com.example.backendredu.comentario.domain.ComentarioService;
import com.example.backendredu.comentario.dto.ComentarioRequestDto;
import com.example.backendredu.comentario.dto.ComentarioResponseDto;
import com.example.backendredu.auth.JwtService;
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

@WebMvcTest(controllers = PublicacionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PublicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublicacionService publicacionService;

    @MockBean
    private ComentarioService comentarioService;

    @MockBean
    private JwtService jwtService;

    private PublicacionResponseDto publicacionDto;
    private PublicacionRequestDto requestDto;

    @BeforeEach
    void setUp() {
        publicacionDto = new PublicacionResponseDto(
                1L,
                "admin",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Test Publicacion",
                "Descripcion de prueba",
                List.of("CODING", "EDUCACION"),
                false,
        );

        requestDto = new PublicacionRequestDto(
                "Nueva Publicacion",
                "Nueva descripcion",
                List.of(Tag.EDUCACION, Tag.DEBATE)
        );
    }

    @Test
    void listarPublicaciones_deberiaRetornarLista() throws Exception {
        when(publicacionService.allPublicaciones()).thenReturn(List.of(publicacionDto));

        mockMvc.perform(get("/publicacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Test Publicacion"))
                .andExpect(jsonPath("$[0].autorUsername").value("admin"));

        verify(publicacionService).allPublicaciones();
    }

    @Test
    void getPublicacion_deberiaRetornarPublicacionPorId() throws Exception {
        when(publicacionService.getPublicacionById(1L)).thenReturn(publicacionDto);

        mockMvc.perform(get("/publicacion/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Test Publicacion"))
                .andExpect(jsonPath("$.descripcion").value("Descripcion de prueba"))
                .andExpect(jsonPath("$.autorUsername").value("admin"));

        verify(publicacionService).getPublicacionById(1L);
    }

    @Test
    @WithMockUser(username = "admin@correo.com", roles = {"ADMINISTRADOR"})
    void crearPublicacion_deberiaCrearYDevolverPublicacion() throws Exception {
        when(publicacionService.createPublicacion(any(PublicacionRequestDto.class), eq("admin@correo.com")))
                .thenReturn(publicacionDto);

        mockMvc.perform(post("/publicacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/publicacion/1"))
                .andExpect(jsonPath("$.titulo").value("Test Publicacion"))
                .andExpect(jsonPath("$.autorUsername").value("admin"));

        verify(publicacionService).createPublicacion(any(PublicacionRequestDto.class), eq("admin@correo.com"));
    }

    @Test
    @WithMockUser(username = "admin@correo.com", roles = {"DIRECTIVA"})
    void actualizarPublicacion_deberiaActualizarCampos() throws Exception {
        PublicacionUpdateDto updateDto = new PublicacionUpdateDto();
        updateDto.setTitulo("Titulo Actualizado");

        PublicacionResponseDto updatedDto = new PublicacionResponseDto(
                1L,
                "admin",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Titulo Actualizado",
                "Descripcion de prueba",
                List.of("CODING", "EDUCACION"),
                false
        );

        when(publicacionService.actualizarPublicacion(any(PublicacionUpdateDto.class), eq(1L), eq("admin@correo.com")))
                .thenReturn(updatedDto);

        mockMvc.perform(patch("/publicacion/actualizar/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Titulo Actualizado"));

        verify(publicacionService).actualizarPublicacion(any(PublicacionUpdateDto.class), eq(1L), eq("admin@correo.com"));
    }

    @WithMockUser(username = "alumno3@gmail.com", roles = "USER")
    @Test
    void comentarPublicacion_deberiaAgregarComentario() throws Exception {
        ComentarioRequestDto comentarioDto = new ComentarioRequestDto();
        comentarioDto.setContenido("Excelente publicacion!");

        ComentarioResponseDto responseDto = new ComentarioResponseDto();
        responseDto.setAutor("alumno3@gmail.com");
        responseDto.setId(1L);
        responseDto.setContenido("Excelente publicacion!");

        when(comentarioService.crearComentario(eq("alumno3@gmail.com"), any(ComentarioRequestDto.class), eq(1L)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/publicacion/{id}/comentar", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/publicacion/1"))
                .andExpect(jsonPath("$.contenido").value("Excelente publicacion!"));

        verify(comentarioService).crearComentario(eq("alumno3@gmail.com"),any(ComentarioRequestDto.class), eq(1L));
    }

    @Test
    void listarComentarios_deberiaListarComentariosDeUnaPublicacion() throws Exception {
        ComentarioResponseDto comentario1 = new ComentarioResponseDto();
        comentario1.setId(1L);
        comentario1.setContenido("Primer comentario");

        ComentarioResponseDto comentario2 = new ComentarioResponseDto();
        comentario2.setId(2L);
        comentario2.setContenido("Segundo comentario");

        when(comentarioService.listarComentarios(1L))
                .thenReturn(List.of(comentario1, comentario2));

        mockMvc.perform(get("/publicacion/{id}/comentarios", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].contenido").value("Primer comentario"))
                .andExpect(jsonPath("$[1].contenido").value("Segundo comentario"));

        verify(comentarioService).listarComentarios(1L);
    }
}