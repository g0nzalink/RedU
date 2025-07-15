package com.example.backendredu.publicacion.infrastructure;

import com.example.backendredu.publicacion.domain.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
	List<Publicacion> findAllByOrderByFechaPublicacionDesc();

	@Query("SELECT p FROM Publicacion p JOIN FETCH p.autor WHERE p.id = :id")
	Optional<Publicacion> findByIdWithAutor(@Param("id") Long id);

	@Query("""
      SELECT p
      FROM Publicacion p
      JOIN FETCH p.autor
      LEFT JOIN FETCH p.likes
      WHERE p.id = :id
      """)
	Optional<Publicacion> findByIdWithAutorAndLikes(@Param("id") Long id);

	@Query("""
      SELECT p
      FROM Publicacion p
      JOIN FETCH p.autor
      LEFT JOIN FETCH p.listComentario
      WHERE p.id = :id
      """)
	Optional<Publicacion> findByIdWithAutorAndComentarios(@Param("id") Long id);
}

