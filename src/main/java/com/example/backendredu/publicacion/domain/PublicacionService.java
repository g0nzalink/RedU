package com.example.backendredu.publicacion.domain;

import com.example.backendredu.Like.domain.Like;
import com.example.backendredu.Like.infrastructure.LikeRepository;
import com.example.backendredu.cloudinary.CloudinaryService;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.exceptions.ClubNotFoundException;
import com.example.backendredu.club.infrastructure.ClubRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final LikeRepository likeRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    
    
    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion p = publicacionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));
        
        PublicacionResponseDto dto = modelMapper.map(p, PublicacionResponseDto.class);
        
        if (p.getAutor() != null) {
            dto.setAutorUsername(p.getAutor().getUsername());
            dto.setCreador(p.getAutor().getEmail());
        }
        
        dto.setLikesCount(p.getLikes().size());
        dto.setLikedByCurrentUser(false);
        dto.setFotoUrl(p.getFotoUrl());
        
        return dto;
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
    
    public String subirImagen(Long id, String emailUsuario, MultipartFile file) throws IOException {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada"));
        
        if (!publicacion.getAutor().getEmail().equals(emailUsuario)) {
            throw new AccessDeniedException("No tienes permiso para subir imagen a esta publicación");
        }
        
        String url = cloudinaryService.uploadImage(file, "publicaciones", id.toString());
        
        publicacion.setFotoUrl(url);
        publicacionRepository.save(publicacion);
        return url;
    }
}

