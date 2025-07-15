package com.example.backendredu.proyecto.domain;

import com.example.backendredu.cloudinary.CloudinaryService;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.proyecto.dto.ProyectoRequestDto;
import com.example.backendredu.proyecto.dto.ProyectoResponseDto;
import com.example.backendredu.proyecto.infrastructure.ProyectoRepository;
import com.example.backendredu.publicacion.dto.PaginatedResponse;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    private final UsuarioRepository usuarioRepository;

    private final ModelMapper modelMapper;
    
    private final ClubRepository clubRepository;
    private final CloudinaryService cloudinaryService;

    private ProyectoResponseDto convertirAProyectoDto(Proyecto proyecto) {
        ProyectoResponseDto dto = new ProyectoResponseDto();
        dto.setId(proyecto.getId());
        dto.setTitulo(proyecto.getTitulo());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setFechaPublicacion(proyecto.getFechaPublicacion());
        dto.setFechaModificacion(proyecto.getFechaModificacion());
        dto.setAutorUsername(proyecto.getAutor().getUsername());
        dto.setCreador(proyecto.getAutor().getEmail());
        dto.setListTag(proyecto.getListTag().stream().map(Enum::name).toList());

        dto.setEsProyecto(true);
        dto.setLikesCount(proyecto.getLikes().size());
        dto.setLikedByCurrentUser(false);
        dto.setCapacidad(proyecto.getCapacidad());
        dto.setStatus(proyecto.getStatus());
        dto.setFotoUrl(proyecto.getFotoUrl());
        
        return dto;
    }
    
    public PaginatedResponse<ProyectoResponseDto> paginateProyectos(String username, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("fechaPublicacion").descending());
        Page<Proyecto> proyectosPage = proyectoRepository.findAll(pageable);
        
        List<ProyectoResponseDto> dtos = proyectosPage.getContent().stream()
                .map(proyecto -> {
                    ProyectoResponseDto dto = new ProyectoResponseDto();
                    dto.setId(proyecto.getId());
                    dto.setTitulo(proyecto.getTitulo());
                    dto.setDescripcion(proyecto.getDescripcion());
                    dto.setFechaPublicacion(proyecto.getFechaPublicacion());
                    dto.setFechaModificacion(proyecto.getFechaModificacion());
                    dto.setCapacidad(proyecto.getCapacidad());
                    dto.setStatus(proyecto.getStatus());
                    dto.setEsProyecto(true);
                    dto.setFotoUrl(proyecto.getFotoUrl());
                    dto.setLikesCount(proyecto.getLikes().size());
                    dto.setLikedByCurrentUser(
                            proyecto.getLikes().stream()
                                    .anyMatch(like -> like.getUsuario().getEmail().equals(username))
                    );
                    
                    if (proyecto.getAutor() != null) {
                        dto.setCreador(proyecto.getAutor().getEmail());
                        dto.setAutorUsername(proyecto.getAutor().getUsername());
                    }
                    
                    if (proyecto.getListTag() != null) {
                        dto.setListTag(proyecto.getListTag().stream().map(Enum::name).toList());
                    }
                    
                    return dto;
                })
                .toList();
        
        return new PaginatedResponse<>(dtos, proyectosPage.hasNext());
    }
    
    
    @Transactional
    public ProyectoResponseDto crearProyecto(ProyectoRequestDto dto, String email, MultipartFile imagen) {
        Usuario autor = usuarioRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        
        Proyecto entidad = modelMapper.map(dto, Proyecto.class);
        entidad.setAutor(autor);
        entidad.setEsProyecto(true);
        entidad.setFechaPublicacion(LocalDateTime.now());
        entidad.setStatus(dto.getStatus());
        
        if (imagen != null && !imagen.isEmpty()) {
            try {
                String url = cloudinaryService.uploadImage(imagen, "proyectos", "proy_" + UUID.randomUUID());
                entidad.setFotoUrl(url);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir imagen", e);
            }
        }
        
        Proyecto saved = proyectoRepository.save(entidad);
        ProyectoResponseDto responseDto = modelMapper.map(saved, ProyectoResponseDto.class);
        
        responseDto.setAutorUsername(autor.getUsername());
        responseDto.setCreador(autor.getEmail());
        
        return responseDto;
    }
    
    
    @Transactional
    public List<ProyectoResponseDto> allProyectos() {
        return proyectoRepository.findAll().stream()
                .map(this::convertirAProyectoDto)
                .collect(Collectors.toList());
    }

    public ProyectoResponseDto obtenerProyecto(Long id) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe"));
        return convertirAProyectoDto(p);
    }

    @Transactional
    public ProyectoResponseDto actualizarProyecto(
            ProyectoRequestDto dto,
            Long proyectoId,
            String emailLogeado,
            MultipartFile imagen
    ) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado"));

        // Verificar autor
        String emailAutor = proyecto.getAutor().getEmail();
        if (!emailAutor.equals(emailLogeado)) {
            throw new AccessDeniedException("Solo el autor puede actualizar esta publicación");
        }

        // Actualizar campos opcionales
        if (dto.getStatus() != null) {
            proyecto.setStatus(dto.getStatus());
        }
        if (dto.getTitulo() != null) {
            proyecto.setTitulo(dto.getTitulo());
        }
        if (dto.getDescripcion() != null) {
            proyecto.setDescripcion(dto.getDescripcion());
        }
        if (dto.getListTag() != null) {
            proyecto.setListTag(dto.getListTag());
        }
        if (dto.getCapacidad() != null) {
            proyecto.setCapacidad(dto.getCapacidad());
        }

        // Si llega una nueva imagen, la subimos a Cloudinary y actualizamos la URL
        if (imagen != null && !imagen.isEmpty()) {
            try {
                String publicId = "proy_" + UUID.randomUUID();
                String url = cloudinaryService.uploadImage(imagen, "proyectos", publicId);
                proyecto.setFotoUrl(url);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir imagen", e);
            }
        }

        proyecto.setFechaModificacion(LocalDateTime.now());

        Proyecto saved = proyectoRepository.save(proyecto);
        ProyectoResponseDto responseDto = convertirAProyectoDto(saved);

        // Asegurarnos de rellenar autor y creador
        responseDto.setAutorUsername(saved.getAutor().getUsername());
        responseDto.setCreador(saved.getAutor().getEmail());

        return responseDto;
    }

    @Transactional
    public void eliminarProyecto(Long proyectoId, String emailLogeado) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado"));

        String emailAutor = proyecto.getAutor().getEmail();

        Usuario solicitante = usuarioRepository.findByEmail(emailLogeado)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        boolean esAutor = emailAutor.equals(emailLogeado);
        boolean esAdmin = solicitante.getUserType() == Role.ADMINISTRADOR;

        if (!esAutor && !esAdmin) {
            throw new AccessDeniedException("Solo el autor o un administrador puede eliminar este proyecto");
        }

        proyectoRepository.delete(proyecto);
    }


}