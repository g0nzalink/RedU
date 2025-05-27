package com.example.backendredu.publicacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionRequestDto {

    @NotNull
    private LocalDateTime fechaPublicacion;

    @NotNull
    private LocalDateTime fechaModificacion;

    @NotBlank
    private String titulo;

    private String descripcion;

    @NotEmpty
    private List<String> listTag;
}
