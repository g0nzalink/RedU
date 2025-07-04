package com.example.backendredu.proyecto.dto;

import com.example.backendredu.proyecto.domain.Status;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoResponseDto extends PublicacionResponseDto {
    private Status status;
    private Integer capacidad;
}
