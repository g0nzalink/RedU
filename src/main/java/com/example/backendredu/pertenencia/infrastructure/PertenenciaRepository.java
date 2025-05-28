package com.example.backendredu.pertenencia.infrastructure;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.pertenencia.domain.Pertenencia;
import com.example.backendredu.pertenencia.domain.Relacion;
import com.example.backendredu.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PertenenciaRepository extends JpaRepository<Pertenencia, Long> {

	boolean existsByUsuarioIdAndClubId(Usuario usuario, Club club);
	boolean existsByRelacion(Relacion relacion);

	boolean existsByUsuarioIdAndClubIdAndRelacion(Usuario usuario, Club club, Relacion relacion);

	Optional<Pertenencia> findByUsuarioIdEmailAndClubIdEmailAndRelacion(
			String usuarioEmail, String clubEmail, Relacion relacion);

	List<Pertenencia> findByClubIdAndRelacion(Club club, Relacion relacion);

	Optional<Pertenencia> findByUsuarioIdEmailAndClubIdEmail(String userEmail, String clubEmail);
}

