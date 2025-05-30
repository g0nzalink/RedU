package com.example.backendredu.proyecto.dto;

import com.example.backendredu.proyecto.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoResponseDto {
    private Long id;
    private String autor;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaModificacion;
    private String titulo;
    private String descripcion;
    private List<String> listTag;
    private Status status;
    private Integer capacidad;
}
