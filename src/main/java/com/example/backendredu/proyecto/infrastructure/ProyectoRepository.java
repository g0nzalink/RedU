package com.example.backendredu.proyecto.infrastructure;

import com.example.backendredu.proyecto.domain.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
}
