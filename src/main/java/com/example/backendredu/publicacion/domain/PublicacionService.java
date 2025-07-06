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
    private final PertenenciaRepository pertenenciaRepository;
    private final ClubRepository clubRepository;
    private final CloudinaryService cloudinaryService;
    
    @Transactional
    public PublicacionResponseDto createPublicacion(PublicacionRequestDto dto, String emailUsuario, MultipartFile imagen) {
        Usuario autor = usuarioRepository.findById(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + emailUsuario));
        
        Publicacion entidad = modelMapper.map(dto, Publicacion.class);
        entidad.setAutor(autor);
        entidad.setEsProyecto(false);
        entidad.setFechaPublicacion(LocalDateTime.now());
        
        if (imagen != null && !imagen.isEmpty()) {
            try {
                String url = cloudinaryService.uploadImage(imagen, "publicaciones", "pub_" + UUID.randomUUID());
                entidad.setFotoUrl(url);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir imagen", e);
            }
        }
        
        Club club = clubRepository.findById(dto.getClub())
                .orElseThrow(() -> new ClubNotFoundException("No se encontró al club con ID: " + dto.getClub()));
        
        if (autor.getUserType().equals(Role.DIRECTIVA)) {
            boolean pertenece = pertenenciaRepository
                    .findAllByUsuarioIdEmailAndRelacion(emailUsuario, Relacion.DIRECTIVA).stream()
                    .anyMatch(p -> p.getClubId().getEmail().equals(dto.getClub()));
            
            if (!pertenece) {
                throw new AccessDeniedException("El usuario no pertenece a la directiva del club especificado");
            }
            
            entidad.setClub(club);
        } else if (autor.getUserType().equals(Role.ADMINISTRADOR)) {
            entidad.setClub(club);
        } else {
            throw new IllegalStateException("Solo DIRECTIVA o ADMINISTRADOR pueden publicar.");
        }
        
        Publicacion saved = publicacionRepository.save(entidad);
        PublicacionResponseDto dtoResp = modelMapper.map(saved, PublicacionResponseDto.class);
        
        dtoResp.setAutorUsername(autor.getUsername());
        dtoResp.setCreador(autor.getEmail());
        dtoResp.setClubLogoUrl(club.getFotoUrl());
        
        return dtoResp;
    }
    
    
    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion p = publicacionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));
        
        PublicacionResponseDto dto = modelMapper.map(p, PublicacionResponseDto.class);
        
        if (p.getAutor() != null) {
            dto.setAutorUsername(p.getAutor().getUsername());
            dto.setCreador(p.getAutor().getEmail());
        }
        
        if (p.getClub() != null) {
            dto.setClubName(p.getClub().getNombre());
            dto.setClubLogoUrl(p.getClub().getFotoUrl());
            dto.setClubEmail(p.getClub().getEmail());
        } else {
            dto.setClubName("ADMINCLUB");
            dto.setClubEmail(null);
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
                    
                    if (p.getClub() != null) {
                        dto.setClubName(p.getClub().getNombre());
                        dto.setClubLogoUrl(p.getClub().getFotoUrl());
                        dto.setClubEmail(p.getClub().getEmail());
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

        if (dto.getTitulo() != null) { entidad.setTitulo(dto.getTitulo()); }
        if (dto.getDescripcion() != null) { entidad.setDescripcion(dto.getDescripcion()); }
        entidad.setFechaModificacion(LocalDateTime.now());
        if (dto.getListTag() != null) { entidad.setListTag(dto.getListTag()); }

        Publicacion updated = publicacionRepository.save(entidad);
        PublicacionResponseDto responseDto = modelMapper.map(updated, PublicacionResponseDto.class);
        
        responseDto.setAutorUsername(updated.getAutor().getUsername());
        responseDto.setCreador(updated.getAutor().getEmail());
        
        if (updated.getClub() != null) {
            responseDto.setClubName(updated.getClub().getNombre());
            responseDto.setClubLogoUrl(updated.getClub().getFotoUrl());
            responseDto.setClubEmail(updated.getClub().getEmail());
        }
        
        responseDto.setLikesCount(updated.getLikes().size());
        responseDto.setLikedByCurrentUser(
                updated.getLikes().stream().anyMatch(l -> l.getUsuario().getEmail().equals(emailLogeado))
        );
        
        return responseDto;
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

