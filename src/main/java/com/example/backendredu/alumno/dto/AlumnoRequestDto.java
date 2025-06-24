package com.example.backendredu.alumno.dto;

import com.example.backendredu.alumno.domain.Carrera;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String description;

    private String fotoPerfilUrl;

    @NotNull
    private Carrera carrera;

}
