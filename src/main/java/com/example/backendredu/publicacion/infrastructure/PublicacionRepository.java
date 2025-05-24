package com.example.backendredu.publicacion.infrastructure;

import com.example.backendredu.publicacion.domain.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicacionRepository extends JpaRepository<Long, Publicacion> {
}

