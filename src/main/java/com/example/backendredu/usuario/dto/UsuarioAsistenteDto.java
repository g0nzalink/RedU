package com.example.backendredu.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAsistenteDto {
    private String email;
    private String username;
    private String fotoPerfilUrl;
}