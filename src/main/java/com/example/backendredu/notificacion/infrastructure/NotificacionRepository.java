package com.example.backendredu.notificacion.infrastructure;

import com.example.backendredu.notificacion.domain.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByReceptorEmailOrderByFechaCreacionDesc(String email);
}
