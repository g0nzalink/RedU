package com.example.backendredu.comentario.infrastructure;

import com.example.backendredu.comentario.domain.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPublicacionId(Long publicacionId);
}
