package com.example.backendredu.publicacion.domain;

import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.publicacion.dto.PublicacionUpdateDto;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
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
public class PublicacionServiceTest{


    @InjectMocks
    private PublicacionService publicacionService;


    @Mock
    private PublicacionRepository publicacionRepository;


    @Mock
    private UsuarioRepository usuarioRepository;


    @Mock
    private ModelMapper modelMapper;


    private String email;
    private Usuario usuario;
    private Publicacion publicacion;
    private PublicacionRequestDto dto;
    private PublicacionUpdateDto dtoUpdate;
    private PublicacionResponseDto responseDto;


    @BeforeEach
    void setUp() {
        email = "test@correo.com";
        usuario = new Usuario();
        usuario.setEmail(email);


        publicacion = new Publicacion();
        publicacion.setId(1L);
        publicacion.setAutor(usuario);


        dto = new PublicacionRequestDto();
        dto.setTitulo("Título");
        dto.setDescripcion("Descripción");


        dtoUpdate = new PublicacionUpdateDto();
        dtoUpdate.setTitulo("Nuevo título");


        responseDto = new PublicacionResponseDto();
        responseDto.setId(1L);
    }


    @Test
    void createPublicacion_no_error() {
        when(usuarioRepository.findById(email)).thenReturn(Optional.of(usuario));
        when(publicacionRepository.save(any(Publicacion.class))).thenReturn(publicacion);
        when(modelMapper.map(dto, Publicacion.class)).thenReturn(publicacion);
        when(modelMapper.map(publicacion, PublicacionResponseDto.class)).thenReturn(responseDto);


        PublicacionResponseDto result = publicacionService.createPublicacion(dto, email);


        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(publicacionRepository).save(publicacion);
    }


    @Test
    void crearPublicacion_DeberiaLanzarExcepcion_CuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(email)).thenReturn(Optional.empty());


        assertThrows(UsernameNotFoundException.class, () -> {
            publicacionService.createPublicacion(dto, email);
        });


        verify(usuarioRepository).findById(email);
        verifyNoInteractions(publicacionRepository);
    }


    @Test
    void getPublicacionById_no_error() {
        when(publicacionRepository.findById(1L)).thenReturn(Optional.of(publicacion));
        when(modelMapper.map(publicacion, PublicacionResponseDto.class)).thenReturn(responseDto);


        PublicacionResponseDto result = publicacionService.getPublicacionById(1L);


        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(publicacionRepository).findById(1L);
    }


    @Test
    void getPublicacionById_error() {
        when(publicacionRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> {
            publicacionService.getPublicacionById(1L);
        });


        verify(publicacionRepository).findById(1L);
    }


    @Test
    void allPublicaciones_no_error() {
        List<Publicacion> lista = List.of(publicacion);
        when(publicacionRepository.findAll()).thenReturn(lista);
        when(modelMapper.map(publicacion, PublicacionResponseDto.class)).thenReturn(responseDto);


        List<PublicacionResponseDto> result = publicacionService.allPublicaciones();


        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }


    @Test
    void actualizarPublicacion_DeberiaActualizar_CuandoAutorEsElMismo() {
        when(publicacionRepository.findById(1L)).thenReturn(Optional.of(publicacion));
        when(publicacionRepository.save(publicacion)).thenReturn(publicacion);
        when(modelMapper.map(publicacion, PublicacionResponseDto.class)).thenReturn(responseDto);


        PublicacionResponseDto result = publicacionService.actualizarPublicacion(dtoUpdate, 1L, email);


        assertNotNull(result);
        verify(publicacionRepository).save(publicacion);
    }


    @Test
    void actualizarPublicacion_DeberiaLanzarExcepcion_CuandoUsuarioNoEsAutor() {
        Usuario otroUsuario = new Usuario();
        otroUsuario.setEmail("otro@correo.com");
        publicacion.setAutor(otroUsuario);


        when(publicacionRepository.findById(1L)).thenReturn(Optional.of(publicacion));


        assertThrows(AccessDeniedException.class, () -> {
            publicacionService.actualizarPublicacion(dtoUpdate, 1L, email);
        });
    }


    @Test
    void actualizarPublicacion_DeberiaLanzarExcepcion_CuandoPublicacionNoExiste() {
        when(publicacionRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> {
            publicacionService.actualizarPublicacion(dtoUpdate, 1L, email);
        });
    }
}
