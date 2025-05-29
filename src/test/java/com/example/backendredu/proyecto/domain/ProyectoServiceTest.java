package com.example.backendredu.proyecto.domain;

import com.example.backendredu.proyecto.dto.ProyectoRequestDto;
import com.example.backendredu.proyecto.dto.ProyectoResponseDto;
import com.example.backendredu.proyecto.infrastructure.ProyectoRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {

    @InjectMocks
    private ProyectoService proyectoService;

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    private String email;
    private Usuario autor;
    private Proyecto proyecto;
    private ProyectoRequestDto requestDto;
    private ProyectoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        email = "test@correo.com";

        autor = new Usuario();
        autor.setEmail(email);
        autor.setUsername("Autor");

        proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setAutor(autor);

        requestDto = new ProyectoRequestDto();
        requestDto.setTitulo("Proyecto test");

        responseDto = new ProyectoResponseDto();
        responseDto.setId(1L);
    }

    @Test
    void crearProyecto_deberiaCrearCorrectamente() {
        when(usuarioRepository.findById(email)).thenReturn(Optional.of(autor));
        when(modelMapper.map(requestDto, Proyecto.class)).thenReturn(proyecto);
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);
        when(modelMapper.map(proyecto, ProyectoResponseDto.class)).thenReturn(responseDto);

        ProyectoResponseDto result = proyectoService.crearProyecto(requestDto, email);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(proyectoRepository).save(proyecto);
    }

    @Test
    void crearProyecto_deberiaLanzarExcepcion_SiUsuarioNoExiste() {
        when(usuarioRepository.findById(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            proyectoService.crearProyecto(requestDto, email);
        });
    }

    @Test
    void obtenerProyecto_deberiaDevolverProyecto() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(modelMapper.map(proyecto, ProyectoResponseDto.class)).thenReturn(responseDto);

        ProyectoResponseDto result = proyectoService.obtenerProyecto(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerProyecto_deberiaLanzarExcepcion_SiNoExiste() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            proyectoService.obtenerProyecto(1L);
        });
    }

    @Test
    void allProyectos_deberiaDevolverLista() {
        when(proyectoRepository.findAll()).thenReturn(List.of(proyecto));
        when(modelMapper.map(proyecto, ProyectoResponseDto.class)).thenReturn(responseDto);

        List<ProyectoResponseDto> result = proyectoService.allProyectos();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void actualizarProyecto_deberiaActualizar_SiAutorCoincide() {
        ProyectoRequestDto updateDto = new ProyectoRequestDto();
        updateDto.setTitulo("Nuevo titulo");

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);
        when(modelMapper.map(proyecto, ProyectoResponseDto.class)).thenReturn(responseDto);

        ProyectoResponseDto result = proyectoService.actualizarProyecto(updateDto, 1L, email);

        assertNotNull(result);
        verify(proyectoRepository).save(proyecto);
    }

    @Test
    void actualizarProyecto_deberiaLanzarExcepcion_SiNoEsAutor() {
        Usuario otro = new Usuario();
        otro.setEmail("otro@correo.com");
        proyecto.setAutor(otro);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        assertThrows(AccessDeniedException.class, () -> {
            proyectoService.actualizarProyecto(requestDto, 1L, email);
        });
    }

    @Test
    void actualizarProyecto_deberiaLanzarExcepcion_SiNoExiste() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            proyectoService.actualizarProyecto(requestDto, 1L, email);
        });
    }
}