package com.example.backendredu.publicacion.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResponseDto {

    private Long id;
    private String autorUsername;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaModificacion;
    private String titulo;
    private String descripcion;
    private List<String> listTag;
    private Boolean esProyecto;

}
