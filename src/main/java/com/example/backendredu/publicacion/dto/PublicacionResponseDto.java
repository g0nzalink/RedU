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
    private String creador;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaModificacion;
    private String titulo;
    private String descripcion;
    private String clubEmail;
    private List<String> listTag;
    private Boolean esProyecto;
    private String clubName;
    private String clubLogoUrl;
    private Integer likesCount;
    private boolean likedByCurrentUser;
    private String fotoUrl;
}
