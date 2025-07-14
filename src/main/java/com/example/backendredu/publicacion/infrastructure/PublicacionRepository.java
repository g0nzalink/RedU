package com.example.backendredu.publicacion.infrastructure;

import com.example.backendredu.publicacion.domain.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
	List<Publicacion> findAllByOrderByFechaPublicacionDesc();
}

