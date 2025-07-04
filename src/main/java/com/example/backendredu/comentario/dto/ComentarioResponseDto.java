package com.example.backendredu.comentario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioResponseDto {
    private Long id;
    private String contenido;
    private String autor;
    private LocalDateTime fechaPublicacion;
}
