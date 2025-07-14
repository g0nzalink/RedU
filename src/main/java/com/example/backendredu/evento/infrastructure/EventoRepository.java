package com.example.backendredu.evento.infrastructure;

import com.example.backendredu.evento.domain.Evento;
import com.example.backendredu.publicacion.domain.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
	public List<Evento> findAllByOrderByFechaPublicacionDesc();
}
