package com.example.backendredu.notificacion.application;

import com.example.backendredu.notificacion.domain.Notificacion;
import com.example.backendredu.notificacion.domain.NotificacionService;
import com.example.backendredu.notificacion.dto.NotificacionResponseDto;
import com.example.backendredu.usuario.domain.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<NotificacionResponseDto>> listarPaginado(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<NotificacionResponseDto> dtos =
                notificacionService.obtenerNotificacionesPaginadas(
                        userDetails.getUsername(),
                        PageRequest.of(page, size, Sort.by("fechaCreacion").descending())
                );
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.noContent().build();
    }
}