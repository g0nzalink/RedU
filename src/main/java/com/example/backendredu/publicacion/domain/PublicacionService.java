package com.example.backendredu.publicacion.domain;

import com.example.backendredu.Like.domain.Like;
import com.example.backendredu.Like.infrastructure.LikeRepository;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.exceptions.ClubNotFoundException;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.comentario.dto.ComentarioResponseDto;
import com.example.backendredu.pertenencia.domain.Pertenencia;
import com.example.backendredu.pertenencia.domain.Relacion;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.publicacion.dto.PublicacionUpdateDto;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.domain.Role;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final LikeRepository likeRepository;
    private final ModelMapper modelMapper;
    private final PertenenciaRepository pertenenciaRepository;
    private final ClubRepository clubRepository;
    
    @Transactional
    public PublicacionResponseDto createPublicacion(PublicacionRequestDto dto, String emailUsuario) {
        Usuario autor = usuarioRepository.findById(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + emailUsuario));
        
        Publicacion entidad = modelMapper.map(dto, Publicacion.class);
        entidad.setAutor(autor);
        entidad.setEsProyecto(false);
        entidad.setFechaPublicacion(LocalDateTime.now());
        
        Club club = clubRepository.findById(dto.getClub())
                .orElseThrow(() -> new ClubNotFoundException("No se encontró al club con ID: " + dto.getClub()));
        
        if (autor.getUserType().equals(Role.DIRECTIVA)) {
            boolean pertenece = pertenenciaRepository
                    .findAllByUsuarioIdEmailAndRelacion(emailUsuario, Relacion.DIRECTIVA).stream()
                    .anyMatch(p -> p.getClubId().getEmail().equals(dto.getClub()));
            
            if (!pertenece) { throw new AccessDeniedException("El usuario no pertenece a la directiva del club especificado"); }
            entidad.setClub(club);
        }
        
        else if (autor.getUserType().equals(Role.ADMINISTRADOR)) { entidad.setClub(club); }
        else { throw new IllegalStateException("Solo DIRECTIVA o ADMINISTRADOR pueden publicar."); }
        
        Publicacion saved = publicacionRepository.save(entidad);
        PublicacionResponseDto publicacionResponseDto = modelMapper.map(saved, PublicacionResponseDto.class);
        
        if (saved.getAutor() != null) {
            publicacionResponseDto.setAutorUsername(saved.getAutor().getUsername());
            publicacionResponseDto.setClubLogoUrl(saved.getClub().getFotoUrl());
            publicacionResponseDto.setCreador(saved.getAutor().getEmail());
            
        }
        
        return publicacionResponseDto;
    }


    @Transactional
    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));
        return modelMapper.map(p, PublicacionResponseDto.class);
    }
    
    @Transactional
    public List<PublicacionResponseDto> allPublicaciones(String emailUsuario) {
        return publicacionRepository.findAll().stream()
                .map(p -> {
                    PublicacionResponseDto dto = modelMapper.map(p, PublicacionResponseDto.class);
                    
                    if (p.getAutor() != null) {
                        dto.setAutorUsername(p.getAutor().getUsername());
                        dto.setCreador(p.getAutor().getEmail());
                    }
                    
                    if (p.getClub() != null) {
                        dto.setClubName(p.getClub().getNombre());
                        dto.setClubLogoUrl(p.getClub().getFotoUrl());
                    }
                    
                    dto.setLikesCount(p.getLikes().size());
                    dto.setLikedByCurrentUser(
                            p.getLikes().stream()
                                    .anyMatch(like -> like.getUsuario().getEmail().equals(emailUsuario))
                    );
                    
                    return dto;
                })
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
    
    @Transactional
    public PublicacionResponseDto newLike(Long publicacionId, String email){
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException("Publicacion no encontrada con id: " + publicacionId));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        
        Optional<Like> existingLike = likeRepository.findByPublicacionIdAndUsuarioEmail(publicacionId, email);
        
        if (existingLike.isPresent()) {
            // 🔁 Ya había like → eliminar
            likeRepository.delete(existingLike.get());
            publicacion.setLikesCount(publicacion.getLikesCount() - 1);
            publicacion.getLikes().removeIf(l -> l.getUsuario().getEmail().equals(email));
        } else {
            // ➕ Nuevo like
            Like newLike = new Like();
            newLike.setPublicacion(publicacion);
            newLike.setUsuario(usuario);
            likeRepository.save(newLike);
            publicacion.setLikesCount(publicacion.getLikesCount() + 1);
            publicacion.getLikes().add(newLike);
        }
        
        Publicacion updated = publicacionRepository.save(publicacion);
        PublicacionResponseDto dto = modelMapper.map(updated, PublicacionResponseDto.class);
        dto.setLikedByCurrentUser(existingLike.isEmpty()); // true si recién dio like, false si retiró
        
        return dto;
    }

    public List<UsuarioResponseDto> getUserLikes(Long publicacionId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada"));

        return publicacion.getLikes().stream()
                .map(Like::getUsuario)
                .map(usuario -> modelMapper.map(usuario, UsuarioResponseDto.class))
                .collect(Collectors.toList());
    }
    
    public boolean wasLikedByUser(Long publicacionId, String email) {
        return likeRepository.existsByPublicacionIdAndUsuarioEmail(publicacionId, email);
    }
}

