package com.example.backendredu.auth;

import com.example.backendredu.usuario.domain.Rol;
import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String username;
    private String password;
    private Rol role;
}
