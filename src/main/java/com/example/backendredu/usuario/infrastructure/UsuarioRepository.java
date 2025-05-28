package com.example.backendredu.usuario.infrastructure;

import com.example.backendredu.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);
}

