package com.example.backendredu.alumno.dto;

import com.example.backendredu.alumno.domain.Carrera;
import com.example.backendredu.usuario.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoResponseDto {

    private String email;
    private String username;
    private String description;
    private String fotoPerfilUrl;
    private Role userType;
    private Carrera carrera;

}
