package com.example.backendredu.profesor.dto;

import com.example.backendredu.profesor.domain.Departamento;
import com.example.backendredu.usuario.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorResponseDto {

    private String email;
    private String username;
    private String description;
    private String fotoPerfilUrl;
    private Role userType;
    private Departamento departamento;

}
