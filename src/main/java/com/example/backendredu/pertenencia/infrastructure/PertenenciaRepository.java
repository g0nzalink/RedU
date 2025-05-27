package com.example.backendredu.pertenencia.infrastructure;

import com.example.backendredu.pertenencia.domain.Pertenencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PertenenciaRepository extends JpaRepository<Pertenencia, Long> {
	
	List<Pertenencia> findByClubIdEmail(String email);
	
	Optional<Pertenencia> findByUsuarioIdEmailAndClubIdEmail(String userEmail, String clubEmail);
}

