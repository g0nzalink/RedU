package com.example.backendredu.evento.infrastructure;

import com.example.backendredu.evento.domain.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
