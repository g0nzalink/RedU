package com.example.backendredu.usuario.infrastructure;

import com.example.backendredu.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<String, Usuario> { }

