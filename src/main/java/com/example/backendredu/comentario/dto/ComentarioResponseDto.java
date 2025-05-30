package com.example.backendredu.comentario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioResponseDto {
    private Long id;
    private String contenido;
    private String autor;
}
