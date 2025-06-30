package com.example.backendredu.Like.infrastructure;

import com.example.backendredu.Like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
	boolean existsByPublicacionIdAndUsuarioEmail(Long publicacionId, String email);
	
	Optional<Like> findByPublicacionIdAndUsuarioEmail(Long publicacionId, String usuarioEmail);
}
