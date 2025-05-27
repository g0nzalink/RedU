package com.example.backendredu.comentario.infrastructure;

import com.example.backendredu.comentario.domain.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> { }
