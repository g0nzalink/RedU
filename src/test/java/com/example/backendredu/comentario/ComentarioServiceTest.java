package com.example.backendredu.comentario;

import com.example.backendredu.comentario.domain.Comentario;
import com.example.backendredu.comentario.domain.ComentarioService;
import com.example.backendredu.comentario.dto.ComentarioRequestDto;
import com.example.backendredu.comentario.dto.ComentarioResponseDto;
import com.example.backendredu.comentario.infrastructure.ComentarioRepository;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComentarioServiceTest {
	
	private ComentarioRepository comentarioRepository;
	private PublicacionRepository publicacionRepository;
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	private ComentarioService comentarioService;
	/*
	@BeforeEach
	void setUp() {
		comentarioRepository = mock(ComentarioRepository.class);
		publicacionRepository = mock(PublicacionRepository.class);
		usuarioRepository = mock(UsuarioRepository.class);
		modelMapper = mock(ModelMapper.class);
		
		comentarioService = new ComentarioService(
				comentarioRepository,
				publicacionRepository,
				usuarioRepository,
				modelMapper
		);
	}
*/
	@Test
	void testCrearComentario() {
		ComentarioRequestDto requestDto = new ComentarioRequestDto();
		requestDto.setContenido("Comentario de prueba");

		Publicacion publicacion = new Publicacion();
		publicacion.setId(1L);
		publicacion.setListComentario(new ArrayList<>());

		Usuario usuarioMock = new Usuario();
		usuarioMock.setUsername("tilin@gmail.com");

		Comentario comentarioMock = new Comentario();
		comentarioMock.setAutor("tilin@gmail.com");
		comentarioMock.setContenido("Comentario de prueba");
		comentarioMock.setFechaPublicacion(LocalDateTime.now());

		when(usuarioRepository.findByEmail("tilin@gmail.com"))
				.thenReturn(Optional.of(usuarioMock));

		when(publicacionRepository.findById(1L))
				.thenReturn(Optional.of(publicacion));

		when(modelMapper.map(requestDto, Comentario.class))
				.thenReturn(comentarioMock);

		when(comentarioRepository.save(any(Comentario.class)))
				.thenAnswer(inv -> inv.getArgument(0));

		when(modelMapper.map(any(Comentario.class), eq(ComentarioResponseDto.class)))
				.thenAnswer(inv -> {
					Comentario c = inv.getArgument(0);
					ComentarioResponseDto dto = new ComentarioResponseDto();
					dto.setContenido(c.getContenido());
					return dto;
				});

		ComentarioResponseDto responseDto =
				comentarioService.crearComentario("tilin@gmail.com", requestDto, 1L);

		assertEquals("Comentario de prueba", responseDto.getContenido());
		assertEquals(1, publicacion.getListComentario().size());
	}
	
	@Test
	void testListarComentarios() {
		Publicacion pub = new Publicacion();
		pub.setId(1L);
		
		Comentario comentario1 = new Comentario();
		comentario1.setId(10L);
		comentario1.setContenido("Comentario 1");
		comentario1.setPublicacion(pub);
		
		Comentario comentario2 = new Comentario();
		comentario2.setId(11L);
		comentario2.setContenido("Comentario 2");
		comentario2.setPublicacion(pub);
		
		when(comentarioRepository.findByPublicacionId(1L)).thenReturn(List.of(comentario1, comentario2));
		
		
		when(modelMapper.map(eq(comentario1), eq(ComentarioResponseDto.class)))
				.thenAnswer(inv -> {
					Comentario c = inv.getArgument(0);
					ComentarioResponseDto dto = new ComentarioResponseDto();
					dto.setContenido(c.getContenido());
					return dto;
				});
		
		when(modelMapper.map(eq(comentario2), eq(ComentarioResponseDto.class)))
				.thenAnswer(inv -> {
					Comentario c = inv.getArgument(0);
					ComentarioResponseDto dto = new ComentarioResponseDto();
					dto.setContenido(c.getContenido());
					return dto;
				});
		List<ComentarioResponseDto> resultado = comentarioService.listarComentarios(1L);
		
		assertEquals(2, resultado.size());
		assertEquals("Comentario 1", resultado.get(0).getContenido());
		assertEquals("Comentario 2", resultado.get(1).getContenido());
	}
}
