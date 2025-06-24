package com.example.backendredu.publicacion.domain;

import com.example.backendredu.Like.domain.Like;
import com.example.backendredu.Like.infrastructure.LikeRepository;
import com.example.backendredu.comentario.dto.ComentarioResponseDto;
import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.publicacion.dto.PublicacionUpdateDto;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.dto.UsuarioResponseDto;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final LikeRepository likeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PublicacionResponseDto createPublicacion(PublicacionRequestDto dto, String emailUsuario) {
        Usuario autor = usuarioRepository.findById(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + emailUsuario));

        Publicacion entidad = modelMapper.map(dto, Publicacion.class);
        entidad.setAutor(autor);
        entidad.setEsProyecto(false);
        entidad.setFechaPublicacion(LocalDateTime.now());

        Publicacion saved = publicacionRepository.save(entidad);
        PublicacionResponseDto publicacionResponseDto = modelMapper.map(saved, PublicacionResponseDto.class);
        publicacionResponseDto.setAutorUsername(autor.getUsername());
        return publicacionResponseDto;
    }

    @Transactional
    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));
        return modelMapper.map(p, PublicacionResponseDto.class);
    }

    @Transactional
    public List<PublicacionResponseDto> allPublicaciones() {
        return publicacionRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PublicacionResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PublicacionResponseDto actualizarPublicacion(PublicacionUpdateDto dto, Long id, String emailLogeado) {
        Publicacion entidad = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));

        String emailAutor = entidad.getAutor().getEmail();
        if (!emailAutor.equals(emailLogeado)) {
            throw new AccessDeniedException("Solo el autor puede actualizar esta publicación");
        }

        if (dto.getTitulo() != null)             entidad.setTitulo(dto.getTitulo());
        if (dto.getDescripcion() != null)        entidad.setDescripcion(dto.getDescripcion());
        entidad.setFechaModificacion(LocalDateTime.now());
        if (dto.getListTag() != null) {
            entidad.setListTag(dto.getListTag());
        }

        Publicacion updated = publicacionRepository.save(entidad);
        return modelMapper.map(updated, PublicacionResponseDto.class);
    }

    //Metodo para aniadir un like en una publicacion
    public PublicacionResponseDto newLike(Long publicacionId, String email){
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException("Publicacion no encontrada con id: " + publicacionId));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        Like newLike = new Like();
        newLike.setPublicacion(publicacion);
        newLike.setUsuario(usuario);
        likeRepository.save(newLike);

        publicacion.setLikesCount(publicacion.getLikesCount() + 1);
        publicacion.getLikes().add(newLike);
        Publicacion updated = publicacionRepository.save(publicacion);
        return modelMapper.map(updated, PublicacionResponseDto.class);
    }

    public List<UsuarioResponseDto> getUserLikes(Long publicacionId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada"));

        return publicacion.getLikes().stream()
                .map(Like::getUsuario)
                .map(usuario -> modelMapper.map(usuario, UsuarioResponseDto.class))
                .collect(Collectors.toList());
    }
}

