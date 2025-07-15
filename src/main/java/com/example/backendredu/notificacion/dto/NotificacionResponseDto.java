package com.example.backendredu.notificacion.dto;


import com.example.backendredu.notificacion.domain.TipoNotificacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionResponseDto {
    private Long id;
    private String mensaje;
    private boolean leido;
    private LocalDateTime fechaCreacion;
    private String link;
    private TipoNotificacion tipo;
    private String receptorEmail;
}