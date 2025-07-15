package com.example.backendredu.notificacion.domain;

import com.example.backendredu.notificacion.dto.NotificacionResponseDto;
import com.example.backendredu.notificacion.infrastructure.NotificacionRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void crearNotificacion(String emailReceptor, String mensaje, String link, TipoNotificacion tipo) {
        var receptor = usuarioRepository.findByEmail(emailReceptor)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + emailReceptor));

        var notificacion = Notificacion.builder()
                .receptor(receptor)
                .mensaje(mensaje)
                .link(link)
                .tipo(tipo)
                .fechaCreacion(java.time.LocalDateTime.now())
                .leido(false)
                .build();

        notificacionRepository.save(notificacion);
    }

    @Transactional
    public void marcarComoLeida(Long id) {
        var notif = notificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada: " + id));
        notif.setLeido(true);
        notificacionRepository.save(notif);
    }

    @Transactional()
    public Page<NotificacionResponseDto> obtenerNotificacionesPaginadas(String email, Pageable pageable) {
        return notificacionRepository
                .findByReceptorEmailOrderByFechaCreacionDesc(email, pageable)
                .map(n -> {
                    var dto = modelMapper.map(n, NotificacionResponseDto.class);
                    dto.setReceptorEmail(n.getReceptor().getEmail());
                    return dto;
                });
    }
}