package com.example.backendredu.publicacion.application;

import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.domain.Tag;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PublicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario autor;

    @BeforeEach
    void setUp() {
        autor = new Usuario();
        autor.setEmail("admin@correo.com");
        autor.setPassword("1234");
        autor.setUsername("admin");
        autor.setUserType(Role.ADMINISTRADOR);
    }

    @Test
    void listarPublicaciones_deberiaRetornarListaVacia() throws Exception {
        mockMvc.perform(get("/publicacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void listarPublicaciones_deberiaRetornarPublicacionExistente() throws Exception {
        // Crear una publicación directamente en la base de datos
        Publicacion pub = new Publicacion();
        pub.setAutor(autor);
        pub.setFechaPublicacion(LocalDateTime.now());
        pub.setFechaModificacion(LocalDateTime.now());
        pub.setTitulo("Test Publicacion");
        pub.setDescripcion("Descripcion de prueba");
        pub.setListTag(List.of(Tag.CODING));
        publicacionRepository.save(pub);

        // Verificar que el endpoint devuelve la publicación
        mockMvc.perform(get("/publicacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Test Publicacion"));
    }
}