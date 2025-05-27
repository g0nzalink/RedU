package com.example.backendredu.auth;

import com.example.backendredu.usuario.domain.Role;
import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String username;
    private String password;
    private Role role;
}
