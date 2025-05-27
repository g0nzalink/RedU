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
    private String name;

    @NotBlank
    private String lastNames;

    private String description;

    @NotNull
    private Carrera carrera;

    @NotBlank
    private String facultad;
}
