package com.example.backendredu.evento.domain;

import com.example.backendredu.Like.infrastructure.LikeRepository;
import com.example.backendredu.cloudinary.CloudinaryService;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.exceptions.ClubNotFoundException;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.evento.dto.EventoRequestDto;
import com.example.backendredu.evento.dto.EventoResponseDto;
import com.example.backendredu.evento.infrastructure.EventoRepository;
import com.example.backendredu.pertenencia.domain.Pertenencia;
import com.example.backendredu.pertenencia.domain.Relacion;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.dto.UsuarioAsistenteDto;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final UsuarioRepository usuarioRepository;
    private final ClubRepository clubRepository;
    private final PertenenciaRepository pertenenciaRepository;
    private final EventoRepository eventoRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    private EventoResponseDto convertirAEventoDto(Evento evento) {
        EventoResponseDto dto = new EventoResponseDto();

        dto.setId(evento.getId());
        dto.setTitulo(evento.getTitulo());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaPublicacion(evento.getFechaPublicacion());
        dto.setFechaModificacion(evento.getFechaModificacion());
        dto.setAutorUsername(evento.getAutor().getUsername());
        dto.setCreador(evento.getAutor().getEmail());
        dto.setListTag(evento.getListTag().stream().map(Enum::name).toList());

        dto.setEsProyecto(false);
        dto.setLikesCount(evento.getLikes().size());
        dto.setLikedByCurrentUser(false);
        dto.setFotoUrl(evento.getFotoUrl());

        dto.setClubEmail(evento.getClub().getEmail());
        dto.setClubName(evento.getClub().getNombre());
        dto.setClubLogoUrl(evento.getClub().getFotoUrl());

        dto.setFecha(evento.getFecha());
        dto.setLugar(evento.getLugar());

        return dto;
    }

    @Transactional
    public EventoResponseDto crearEvento(EventoRequestDto dto, String emailUsuario, MultipartFile imagen) {
        Usuario autor = usuarioRepository.findById(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + emailUsuario));

        Pertenencia pertenencia = pertenenciaRepository
                .findByUsuarioIdEmailAndRelacion(emailUsuario, Relacion.DIRECTIVA)
                .orElseThrow(() -> new IllegalStateException("El usuario no es directiva de ningún club."));

        Club club = pertenencia.getClubId();

        if (!(autor.getUserType().equals(Role.DIRECTIVA) || autor.getUserType().equals(Role.ADMINISTRADOR))) {
            throw new IllegalStateException("Solo DIRECTIVA o ADMINISTRADOR pueden crear eventos.");
        }

        Evento evento = new Evento();
        evento.setTitulo(dto.getTitulo());
        evento.setDescripcion(dto.getDescripcion());
        evento.setListTag(dto.getListTag());
        evento.setClub(club);
        evento.setAutor(autor);
        evento.setFecha(dto.getFecha());
        evento.setLugar(dto.getLugar());
        evento.setFechaPublicacion(LocalDateTime.now());
        evento.setEsProyecto(false);

        if (imagen != null && !imagen.isEmpty()) {
            try {
                String url = cloudinaryService.uploadImage(imagen, "eventos", "event_" + UUID.randomUUID());
                evento.setFotoUrl(url);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir imagen", e);
            }
        }

        Evento guardado = eventoRepository.save(evento);
        
        EventoResponseDto respuesta = convertirAEventoDto(guardado);

        return respuesta;
    }


    @Transactional
    public List<EventoResponseDto> allEventos() {
        return eventoRepository.findAllByOrderByFechaPublicacionDesc().stream()
                .map(this::convertirAEventoDto)
                .collect(Collectors.toList());
    }

    public EventoResponseDto obtenerEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe el evento con id: " + id));
        return convertirAEventoDto(evento);
    }

    @Transactional
    public EventoResponseDto actualizarEvento(EventoRequestDto nuevoEvento, Long eventoId, String emailLogeado) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        String emailAutor = evento.getAutor().getEmail();
        if (!emailAutor.equals(emailLogeado)) {
            throw new AccessDeniedException("Solo el autor puede actualizar este evento");
        }

        if (nuevoEvento.getTitulo() != null) {
            evento.setTitulo(nuevoEvento.getTitulo());
        }
        if (nuevoEvento.getDescripcion() != null) {
            evento.setDescripcion(nuevoEvento.getDescripcion());
        }
        if (nuevoEvento.getLugar() != null) {
            evento.setLugar(nuevoEvento.getLugar());
        }
        if (nuevoEvento.getFecha() != null) {
            evento.setFecha(nuevoEvento.getFecha());
        }
        if (nuevoEvento.getListTag() != null) {
            evento.setListTag(nuevoEvento.getListTag());
        }

        evento.setFechaModificacion(LocalDateTime.now());

        Evento actualizado = eventoRepository.save(evento);
        return convertirAEventoDto(actualizado);
    }

    @Transactional
    public void confirmarAsistencia(Long eventoId, String emailUsuario) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        Usuario usuario = usuarioRepository.findById(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!evento.getAsistentes().contains(usuario)) {
            evento.getAsistentes().add(usuario);
        }

        eventoRepository.save(evento);
    }

    @Transactional
    public void cancelarAsistencia(Long eventoId, String emailUsuario) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        Usuario usuario = usuarioRepository.findById(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        evento.getAsistentes().removeIf(u -> u.getEmail().equals(emailUsuario));
        eventoRepository.save(evento);
    }

    @Transactional
    public List<UsuarioAsistenteDto> obtenerAsistentesDto(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        return evento.getAsistentes().stream()
                .map(u -> new UsuarioAsistenteDto(u.getEmail(), u.getUsername(), u.getFotoPerfilUrl()))
                .collect(Collectors.toList());
    }
    
    public void eliminarEvento(Long eventoId) { eventoRepository.deleteById(eventoId); }
}
