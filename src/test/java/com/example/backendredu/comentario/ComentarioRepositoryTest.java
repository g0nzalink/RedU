package com.example.backendredu.comentario;

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

@Testcontainers
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ComentarioRepositoryTest {
	
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3")
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	@Autowired
	private PublicacionRepository publicacionRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private Publicacion publicacion;
	private Comentario comentario1;
	private Comentario comentario2;
	
	@BeforeEach
	void setUp() {
		Usuario autor = new Usuario();
		autor.setEmail("usuario@utec.edu.pe");
		autor.setPassword("123");
		autor.setUsername("usuario123");
		autor.setDescription("Usuario de prueba");
		autor.setUserType(Role.ALUMNO);
		autor = usuarioRepository.save(autor);
		
		publicacion = new Publicacion();
		publicacion.setAutor(autor);
		publicacion.setFechaPublicacion(LocalDateTime.now());
		publicacion.setFechaModificacion(LocalDateTime.now());
		publicacion.setTitulo("Título de prueba");
		publicacion.setDescripcion("Descripción");
		publicacion.setListTag(List.of());
		publicacion.setEsProyecto(false);
		publicacion = publicacionRepository.save(publicacion);
		
		comentario1 = new Comentario();
		comentario1.setContenido("Primer comentario");
		comentario1.setPublicacion(publicacion);
		comentario1.setFechaPublicacion(LocalDateTime.now());
		
		comentario2 = new Comentario();
		comentario2.setContenido("Segundo comentario");
		comentario2.setPublicacion(publicacion);
		comentario2.setFechaPublicacion(LocalDateTime.now());
		
		comentarioRepository.saveAll(List.of(comentario1, comentario2));
	}
	
	@Test
	void findByPublicacionIdShouldReturnComentarios() {
		List<Comentario> comentarios = comentarioRepository.findByPublicacionId(publicacion.getId());
		
		assertThat(comentarios).hasSize(2);
		assertThat(comentarios).extracting(Comentario::getContenido)
				.containsExactlyInAnyOrder("Primer comentario", "Segundo comentario");
	}
	
	@Test
	void saveShouldPersistComentario() {
		Comentario nuevo = new Comentario();
		nuevo.setContenido("Tercer comentario");
		nuevo.setPublicacion(publicacion);
		nuevo.setFechaPublicacion(LocalDateTime.now());
		
		Comentario saved = comentarioRepository.save(nuevo);
		
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getContenido()).isEqualTo("Tercer comentario");
	}
	
	@Test
	void findByPublicacionIdWithNoComentariosShouldReturnEmptyList() {
		Publicacion nueva = new Publicacion();
		nueva.setAutor(publicacion.getAutor());
		nueva.setFechaPublicacion(LocalDateTime.now());
		nueva.setFechaModificacion(LocalDateTime.now());
		nueva.setTitulo("Otra publicación");
		nueva.setDescripcion("Otra descripción");
		nueva.setListTag(List.of());
		nueva.setEsProyecto(false);
		nueva = publicacionRepository.save(nueva);
		
		List<Comentario> comentarios = comentarioRepository.findByPublicacionId(nueva.getId());
		
		assertThat(comentarios).isEmpty();
	}
}
