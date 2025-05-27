package com.example.backendredu.pertenencia.dto;

import com.example.backendredu.pertenencia.domain.Relacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PertenenciaRequestDto {
    private String clubEmail;
    private Relacion relacion;
}
