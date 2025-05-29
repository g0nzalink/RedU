package com.example.backendredu.profesor.infrastructure;

import com.example.backendredu.profesor.domain.Departamento;
import com.example.backendredu.profesor.domain.Profesor;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class ProfesorRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private ProfesorRepository profesorRepository;

    private Profesor profesor1;
    private Profesor profesor2;

    @BeforeEach
    void setUp() {
        profesor1 = new Profesor();
        profesor1.setEmail("micky.mouse@utec.edu.pe");
        profesor1.setUsername("MickyMouse");
        profesor1.setPassword("lS9vw76");
        profesor1.setDepartamento(Departamento.CIENCIAS);
        profesorRepository.save(profesor1);

        profesor2 = new Profesor();
        profesor2.setEmail("goofy@utec.edu.pe");
        profesor2.setUsername("Goofy");
        profesor2.setPassword("1O9gz75");
        profesor2.setDepartamento(Departamento.CIENCIAS);
        profesorRepository.save(profesor2);
    }

    @Test
    void findAllShouldReturnAllProfesores() {
        List<Profesor> profesores = profesorRepository.findAll();

        assertThat(profesores).hasSize(2);
        assertThat(profesores).extracting(Profesor::getEmail)
                .containsExactlyInAnyOrder("micky.mouse@utec.edu.pe", "goofy@utec.edu.pe");
    }

    @Test
    void saveShouldPersistProfesor() {
        Profesor nuevo = new Profesor();
        nuevo.setEmail("patricio.estrella@utec.edu.pe");
        nuevo.setUsername("PatricioEstrella");
        nuevo.setPassword("6O>oQ@5");
        nuevo.setDepartamento(Departamento.CIENCIAS);

        Profesor saved = profesorRepository.save(nuevo);

        assertThat(saved.getEmail()).isEqualTo("patricio.estrella@utec.edu.pe");
        assertThat(saved.getUsername()).isEqualTo("PatricioEstrella");
        assertThat(saved.getDepartamento()).isEqualTo(Departamento.CIENCIAS);
    }

    @Test
    void findByIdWithNonExistingEmailShouldReturnEmpty() {
        var profesor = profesorRepository.findById("pluto@utec.edu.pe");

        assertThat(profesor).isEmpty();
    }
}
