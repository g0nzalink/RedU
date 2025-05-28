package com.example.backendredu.proyecto.dto;

import com.example.backendredu.proyecto.domain.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoRequestDto {

    @NotBlank
    private String autor;

    @NotNull
    private LocalDateTime fechaPublicacion;

    // opcional en update, pero puedes requerirlo en create
    @NotNull
    private LocalDateTime fechaModificacion;

    @NotBlank
    private String titulo;

    private String descripcion;

    @NotEmpty
    private List<String> listTag;

    @NotNull
    @Min(1)
    private Integer capacidad;

    private Status status;
}
