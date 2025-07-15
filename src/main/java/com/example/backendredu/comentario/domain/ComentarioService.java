package com.example.backendredu.comentario.domain;

import com.example.backendredu.comentario.infrastructure.ComentarioRepository;
import com.example.backendredu.notificacion.domain.NotificacionService;
import com.example.backendredu.notificacion.domain.TipoNotificacion;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.comentario.dto.ComentarioRequestDto;
import com.example.backendredu.comentario.dto.ComentarioResponseDto;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    private final PublicacionRepository publicacionRepository;

    private final UsuarioRepository userRepository;

    private final ModelMapper modelMapper;

    private final NotificacionService notificacionService;

    @Transactional
    public ComentarioResponseDto crearComentario(String autorEmail,
                                                 ComentarioRequestDto newcomentario,
                                                 Long publicacionId) {
        // 1) Cargo la publicación con su autor (y comentarios si quiero sincronizar la lista)
        Publicacion pub = publicacionRepository
                .findByIdWithAutorAndComentarios(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Publicación no encontrada con id: " + publicacionId));

        // 2) Inicializo la colección de comentarios para poder modificarla en memoria
        pub.getListComentario().size();

        // 3) Cargo el usuario que hace el comentario
        Usuario user = userRepository.findByEmail(autorEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + autorEmail));

        // 4) Creo y guardo el comentario
        Comentario comentario = modelMapper.map(newcomentario, Comentario.class);
        comentario.setPublicacion(pub);
        comentario.setFechaPublicacion(LocalDateTime.now());
        comentario.setAutor(user.getEmail());

        Comentario saved = comentarioRepository.save(comentario);

        // 5) Sincronizo la colección bidireccional (sólo en memoria)
        pub.getListComentario().add(saved);

        // 6) Notificación si el autor del comentario no es el mismo que el autor de la publicación
        String autorPubEmail = pub.getAutor().getEmail();
        if (!autorPubEmail.equals(user.getEmail())) {
            notificacionService.crearNotificacion(
                    autorPubEmail,                                    // destinatario por email
                    user.getUsername() + " comentó tu publicación",
                    "/publicacion/" + pub.getId(),
                    TipoNotificacion.COMENTARIO
            );
        }

        // 7) Mapeo y retorno del DTO de respuesta
        return modelMapper.map(saved, ComentarioResponseDto.class);
    }

    @Transactional
    public List<ComentarioResponseDto> listarComentarios(Long publicacionId) {
        return comentarioRepository
                .findByPublicacionId(publicacionId)
                .stream()
                .map(c -> modelMapper.map(c, ComentarioResponseDto.class))
                .collect(Collectors.toList());
    }

}
