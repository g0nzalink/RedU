package com.example.backendredu.publicacion.dto;

import com.example.backendredu.publicacion.domain.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionRequestDto {

    @NotBlank
    private String titulo;

    private String descripcion;

    @NotEmpty
    private List<Tag> listTag;
    
    @NotBlank
    private String club;
}
