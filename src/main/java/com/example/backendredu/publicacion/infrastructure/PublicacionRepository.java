package com.example.backendredu.publicacion.infrastructure;

import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.domain.PublicacionService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
}

