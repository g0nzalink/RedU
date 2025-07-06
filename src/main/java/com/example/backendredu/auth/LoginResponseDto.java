package com.example.backendredu.auth;

import com.example.backendredu.usuario.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
	private String token;
	private String email;
	private String username;
	private Role role;
	private String fotoPerfilUrl;
}
