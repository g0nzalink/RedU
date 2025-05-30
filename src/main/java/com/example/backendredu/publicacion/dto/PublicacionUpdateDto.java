package com.example.backendredu.publicacion.dto;

import com.example.backendredu.publicacion.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionUpdateDto {

    private String titulo;
    private String descripcion;
    private List<Tag> listTag;
}
