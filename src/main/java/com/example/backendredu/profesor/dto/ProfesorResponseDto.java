package com.example.backendredu.profesor.dto;

import com.example.backendredu.alumno.domain.Carrera;
import com.example.backendredu.profesor.domain.Departamento;
import com.example.backendredu.usuario.domain.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorResponseDto {

    private String email;
    private String name;
    private String lastNames;
    private String description;
    private Rol userType;        // puede ser profesor
    private Carrera carrera;
    private Departamento departamento;

}
