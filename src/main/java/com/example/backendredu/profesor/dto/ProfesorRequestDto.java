package com.example.backendredu.profesor.dto;

import com.example.backendredu.profesor.domain.Departamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String description;

    private String fotoDePerfil;

    @NotNull
    private Departamento departamento;
}
