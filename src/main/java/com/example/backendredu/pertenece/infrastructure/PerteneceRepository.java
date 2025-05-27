package com.example.backendredu.pertenece.infrastructure;

import com.example.backendredu.pertenece.domain.Pertenece;
import com.example.backendredu.pertenece.domain.Relacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerteneceRepository extends JpaRepository<Pertenece, Long> {
	
	List<Pertenece> findByClubIdEmail(String email);
	
	Optional<Pertenece> findByUsuarioIdEmailAndClubIdEmail(String userEmail, String clubEmail);
}

