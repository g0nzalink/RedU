package com.example.backendredu.publicacion.infrastructure;

import com.example.backendredu.config.PostgresTestContainerConfig;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;


import java.time.LocalDateTime;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PostgresTestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PublicacionRepositoryTest{

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void guardarYRecuperarPublicacion() {
        Usuario usuario = new Usuario();
        usuario.setUsername("test");
        usuario.setEmail("test@example.com");
        usuario.setUserType(Role.ALUMNO);

        usuarioRepository.save(usuario);

        Publicacion publicacion = new Publicacion();
        publicacion.setAutor(usuario);
        publicacion.setFechaPublicacion(LocalDateTime.now());
        publicacion.setFechaModificacion(LocalDateTime.now());
        publicacion.setTitulo("Título de prueba");
        publicacion.setDescripcion("Descripción de prueba");
        publicacion.setListTag(List.of());
        publicacion.setEsProyecto(true);

        Publicacion saved = publicacionRepository.save(publicacion);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitulo()).isEqualTo("Título de prueba");
    }
}
