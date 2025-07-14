package com.example.backendredu.evento.dto;

import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponseDto extends PublicacionResponseDto {

    private LocalDateTime fecha;
    private String lugar;
}
