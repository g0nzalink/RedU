package com.example.backendredu.proyecto.infrastructure;

import com.example.backendredu.config.PostgresTestContainerConfig;
import com.example.backendredu.proyecto.domain.Proyecto;
import com.example.backendredu.proyecto.domain.Status;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.example.backendredu.publicacion.domain.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PostgresTestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ProyectoRepositoryTest {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setUsername("Test Usuario");
        usuario.setUserType(Role.ALUMNO);
        usuarioRepository.save(usuario);
    }

    @Test
    void guardarProyecto_deberiaGuardarCorrectamente() {
        Proyecto proyecto = new Proyecto();
        proyecto.setAutor(usuario);
        proyecto.setTitulo("Proyecto de prueba");
        proyecto.setDescripcion("Descripcion del proyecto");
        proyecto.setStatus(Status.ACTIVO);
        proyecto.setCapacidad(5);
        proyecto.setEsProyecto(true);
        proyecto.setFechaPublicacion(LocalDateTime.now());
        proyecto.setFechaModificacion(LocalDateTime.now());
        proyecto.setListTag(List.of(Tag.PROFESIONAL));

        Proyecto guardado = proyectoRepository.save(proyecto);

        assertThat(guardado).isNotNull();
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getTitulo()).isEqualTo("Proyecto de prueba");
        assertThat(guardado.getStatus()).isEqualTo(Status.ACTIVO);
    }

    @Test
    void buscarPorId_deberiaEncontrarProyecto() {
        Proyecto proyecto = new Proyecto();
        proyecto.setAutor(usuario);
        proyecto.setTitulo("Proyecto para buscar");
        proyecto.setDescripcion("Descripcion");
        proyecto.setStatus(Status.ACTIVO);
        proyecto.setCapacidad(3);
        proyecto.setEsProyecto(true);
        proyecto.setFechaPublicacion(LocalDateTime.now());
        proyecto.setFechaModificacion(LocalDateTime.now());
        proyecto.setListTag(null);

        Proyecto guardado = proyectoRepository.save(proyecto);

        Optional<Proyecto> buscado = proyectoRepository.findById(guardado.getId());

        assertThat(buscado).isPresent();
        assertThat(buscado.get().getTitulo()).isEqualTo("Proyecto para buscar");
    }

    @Test
    void eliminarProyecto_deberiaEliminarCorrectamente() {
        Proyecto proyecto = new Proyecto();
        proyecto.setAutor(usuario);
        proyecto.setTitulo("Proyecto a eliminar");
        proyecto.setDescripcion("Descripcion");
        proyecto.setStatus(Status.ACTIVO);
        proyecto.setCapacidad(4);
        proyecto.setEsProyecto(true);
        proyecto.setFechaPublicacion(LocalDateTime.now());
        proyecto.setFechaModificacion(LocalDateTime.now());
        proyecto.setListTag(null);

        Proyecto guardado = proyectoRepository.save(proyecto);

        proyectoRepository.deleteById(guardado.getId());

        Optional<Proyecto> eliminado = proyectoRepository.findById(guardado.getId());

        assertThat(eliminado).isNotPresent();
    }
}