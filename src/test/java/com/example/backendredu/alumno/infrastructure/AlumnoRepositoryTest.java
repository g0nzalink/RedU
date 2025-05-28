package com.example.backendredu.alumno.infrastructure;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.alumno.domain.Carrera;
import com.example.backendredu.comentario.domain.Comentario;
import com.example.backendredu.comentario.infrastructure.ComentarioRepository;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class AlumnoRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private AlumnoRepository alumnoRepository;

    private Alumno alumno1;
    private Alumno alumno2;

    @BeforeEach
    void setUp() {
        alumno1 = new Alumno();
        alumno1.setEmail("micky.mouse@utec.edu.pe");
        alumno1.setUsername("MickyMouse");
        alumno1.setPassword("lS9vw76");
        alumno1.setCarrera(Carrera.INGENIERIA_ELECTRONICA);
        alumnoRepository.save(alumno1);

        alumno2 = new Alumno();
        alumno2.setEmail("goofy@utec.edu.pe");
        alumno2.setUsername("Goofy");
        alumno2.setPassword("1O9gz75");
        alumno2.setCarrera(Carrera.INGENIERIA_QUIMICA);
        alumnoRepository.save(alumno2);
    }

    @Test
    void findAllShouldReturnAllAlumnos() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        assertThat(alumnos).hasSize(2);
        assertThat(alumnos).extracting(Alumno::getEmail)
                .containsExactlyInAnyOrder("micky.mouse@utec.edu.pe", "goofy@utec.edu.pe");
    }

    @Test
    void saveShouldPersistAlumno() {
        Alumno nuevo = new Alumno();
        nuevo.setEmail("patricio.estrella@utec.edu.pe");
        nuevo.setUsername("PatricioEstrella");
        nuevo.setPassword("6O>oQ@5");
        nuevo.setCarrera(Carrera.CIENCIA_DE_DATOS);

        Alumno saved = alumnoRepository.save(nuevo);

        assertThat(saved.getEmail()).isEqualTo("patricio.estrella@utec.edu.pe");
        assertThat(saved.getUsername()).isEqualTo("PatricioEstrella");
        assertThat(saved.getCarrera()).isEqualTo(Carrera.CIENCIA_DE_DATOS);
    }

    @Test
    void findByIdWithNonExistingEmailShouldReturnEmpty() {
        var alumno = alumnoRepository.findById("pluto@utec.edu.pe");

        assertThat(alumno).isEmpty();
    }
}