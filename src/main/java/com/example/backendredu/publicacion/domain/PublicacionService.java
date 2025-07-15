package com.example.backendredu.publicacion.domain;

import com.example.backendredu.Like.domain.Like;
import com.example.backendredu.Like.infrastructure.LikeRepository;
import com.example.backendredu.cloudinary.CloudinaryService;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.exceptions.ClubNotFoundException;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.evento.domain.Evento;
import com.example.backendredu.evento.dto.EventoResponseDto;
import com.example.backendredu.notificacion.domain.NotificacionService;
import com.example.backendredu.notificacion.domain.TipoNotificacion;
import com.example.backendredu.pertenencia.domain.Relacion;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.proyecto.domain.Proyecto;
import com.example.backendredu.proyecto.dto.ProyectoResponseDto;
import com.example.backendredu.publicacion.dto.PaginatedResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final NotificacionService notificacionService;
    
    private PublicacionResponseDto mapToDto(Publicacion publicacion, String username) {
        PublicacionResponseDto dto;
        
        if (publicacion instanceof Proyecto proyecto) {
            ProyectoResponseDto proyectoDto = new ProyectoResponseDto();
            proyectoDto.setCapacidad(proyecto.getCapacidad());
            proyectoDto.setStatus(proyecto.getStatus());
            proyectoDto.setFotoUrl(proyecto.getFotoUrl());
            dto = proyectoDto;
        } else if (publicacion instanceof Evento evento) {
            EventoResponseDto eventoDto = new EventoResponseDto();
            eventoDto.setFecha(evento.getFecha());
            eventoDto.setLugar(evento.getLugar());
            
            if (evento.getClub() != null) {
                eventoDto.setClubEmail(evento.getClub().getEmail());
                eventoDto.setClubName(evento.getClub().getNombre());
                eventoDto.setClubLogoUrl(evento.getClub().getFotoUrl());
            }
            
            dto = eventoDto;
        } else {
            dto = new PublicacionResponseDto();
        }
        
        // Campos comunes
        dto.setId(publicacion.getId());
        dto.setTitulo(publicacion.getTitulo());
        dto.setDescripcion(publicacion.getDescripcion());
        dto.setFechaPublicacion(publicacion.getFechaPublicacion());
        dto.setFechaModificacion(publicacion.getFechaModificacion());
        dto.setEsProyecto(publicacion.getEsProyecto());
        dto.setFotoUrl(publicacion.getFotoUrl());
        
        if (publicacion.getAutor() != null) {
            dto.setCreador(publicacion.getAutor().getEmail());
            dto.setAutorUsername(publicacion.getAutor().getUsername());
        }
        
        if (publicacion.getListTag() != null) {
            dto.setListTag(publicacion.getListTag().stream()
                    .map(Enum::name)
                    .toList());
        }
        
        dto.setLikesCount(publicacion.getLikesCount());
        dto.setLikedByCurrentUser(
                publicacion.getLikes().stream()
                        .anyMatch(l -> l.getUsuario().getEmail().equals(username))
        );
        
        return dto;
    }
    
    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));
        
        PublicacionResponseDto dto;
        
        if (p instanceof Evento evento) {
            dto = modelMapper.map(evento, EventoResponseDto.class);
            
            if (evento.getClub() != null) {
                ((EventoResponseDto) dto).setFecha(evento.getFecha());
                ((EventoResponseDto) dto).setLugar(evento.getLugar());
                
                dto.setClubEmail(evento.getClub().getEmail());
                dto.setClubName(evento.getClub().getNombre());
                dto.setClubLogoUrl(evento.getClub().getFotoUrl());
            }
            
        } else if (p instanceof Proyecto proyecto) {
            dto = modelMapper.map(proyecto, ProyectoResponseDto.class);
        } else {
            dto = modelMapper.map(p, PublicacionResponseDto.class);
        }
        
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
        List<Publicacion> publicaciones = publicacionRepository.findAllByOrderByFechaPublicacionDesc();
        
        return publicaciones.stream()
                .map(p -> {
                    PublicacionResponseDto dto = modelMapper.map(p, PublicacionResponseDto.class);
                    
                    // Autor
                    if (p.getAutor() != null) {
                        dto.setAutorUsername(p.getAutor().getUsername());
                        dto.setCreador(p.getAutor().getEmail());
                    }
                    
                    // Likes
                    dto.setLikesCount(p.getLikes().size());
                    dto.setLikedByCurrentUser(
                            p.getLikes().stream()
                                    .anyMatch(like -> like.getUsuario().getEmail().equals(emailUsuario))
                    );
                    
                    // Imagen principal
                    dto.setFotoUrl(p.getFotoUrl());
                    
                    // Si es un evento, asignar datos del club
                    if (p instanceof Evento evento && evento.getClub() != null) {
                        dto.setClubEmail(evento.getClub().getEmail());
                        dto.setClubName(evento.getClub().getNombre());
                        dto.setClubLogoUrl(evento.getClub().getFotoUrl());
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public PublicacionResponseDto newLike(Long publicacionId, String usuarioEmail) {
        // 1) Carga publicación con autor y lista de likes
        Publicacion publicacion = publicacionRepository
                .findByIdWithAutorAndLikes(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Publicación no encontrada con id: " + publicacionId));

        // 2) Aseguramos que la colección de likes esté inicializada
        publicacion.getLikes().size();

        // 3) Carga el usuario que va a dar/quitar like
        Usuario usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + usuarioEmail));

        // 4) Verificamos si ya había like
        Optional<Like> existingLike = likeRepository
                .findByPublicacionIdAndUsuarioEmail(publicacionId, usuarioEmail);

        if (existingLike.isPresent()) {
            // 🔁 Ya había like → eliminarlo
            likeRepository.delete(existingLike.get());
            publicacion.setLikesCount(publicacion.getLikesCount() - 1);
            publicacion.getLikes().removeIf(
                    l -> l.getUsuario().getEmail().equals(usuarioEmail));
        } else {
            // ➕ Nuevo like → agregarlo
            Like newLike = new Like();
            newLike.setPublicacion(publicacion);
            newLike.setUsuario(usuario);
            likeRepository.save(newLike);

            publicacion.setLikesCount(publicacion.getLikesCount() + 1);
            publicacion.getLikes().add(newLike);

            String autorEmail = publicacion.getAutor().getEmail();
            if (!autorEmail.equals(usuarioEmail)) {
                notificacionService.crearNotificacion(
                        autorEmail,                                      // destinatario por email
                        usuario.getUsername() + " le dio like a tu publicación",
                        "/post/" + publicacionId,
                        TipoNotificacion.LIKE
                );
            }
        }

        // 5) Guardamos la publicación y mapeamos a DTO
        Publicacion updated = publicacionRepository.save(publicacion);
        PublicacionResponseDto dto = modelMapper
                .map(updated, PublicacionResponseDto.class);
        dto.setLikedByCurrentUser(existingLike.isEmpty());

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
    
    public PaginatedResponse<PublicacionResponseDto> paginatePublicaciones(String username, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("fechaPublicacion").descending());
        
        Page<Publicacion> publicacionesPage = publicacionRepository.findAll(pageable);
        
        List<PublicacionResponseDto> dtos = publicacionesPage
                .getContent()
                .stream()
                .map(p -> mapToDto(p, username))
                .toList();
        
        boolean hasNext = publicacionesPage.hasNext();
        
        return new PaginatedResponse<>(dtos, hasNext);
    }
    
}

