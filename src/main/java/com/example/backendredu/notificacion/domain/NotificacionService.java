package com.example.backendredu.notificacion.domain;

import com.example.backendredu.notificacion.infrastructure.NotificacionRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public void crearNotificacion(String emailReceptor, String mensaje, String link, TipoNotificacion tipo) {
        Usuario receptor = usuarioRepository.findByEmail(emailReceptor)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Notificacion notificacion = Notificacion.builder()
                .receptor(receptor)
                .mensaje(mensaje)
                .link(link)
                .tipo(tipo)
                .fechaCreacion(LocalDateTime.now())
                .leido(false)
                .build();

        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerNotificaciones(String email) {
        return notificacionRepository.findByReceptorEmailOrderByFechaCreacionDesc(email);
    }

    public void marcarComoLeida(Long id) {
        Notificacion notif = notificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada"));
        notif.setLeido(true);
        notificacionRepository.save(notif);
    }


}

