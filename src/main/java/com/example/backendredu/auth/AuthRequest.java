package com.example.backendredu.auth;

import com.example.backendredu.usuario.domain.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String fotoPerfilUrl;
    private Role role;
}
