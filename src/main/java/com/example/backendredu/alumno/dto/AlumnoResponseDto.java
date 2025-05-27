package com.example.backendredu.alumno.dto;

import com.example.backendredu.alumno.domain.Carrera;
import com.example.backendredu.usuario.domain.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoResponseDto {

    private String email;
    private String name;
    private String lastNames;
    private String description;
    private Rol userType;        // puede ser alumno
    private Carrera carrera;
    private String facultad;

}
