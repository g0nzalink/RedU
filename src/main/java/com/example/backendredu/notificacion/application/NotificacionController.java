package com.example.backendredu.notificacion.application;

import com.example.backendredu.notificacion.domain.Notificacion;
import com.example.backendredu.notificacion.domain.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificacionService.obtenerNotificaciones(userDetails.getUsername()));
    }

    @PostMapping("/{id}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.noContent().build();
    }
}

