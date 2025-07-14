package com.example.backendredu.evento.dto;

import com.example.backendredu.publicacion.domain.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequestDto {

    @NotBlank
    private String titulo;

    private String descripcion;

    @NotEmpty
    private List<Tag> listTag;

    private String fotoUrl;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;

    @NotBlank
    private String lugar;
}
