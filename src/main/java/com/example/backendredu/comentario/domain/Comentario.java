package com.example.backendredu.comentario.domain;

import com.example.backendredu.publicacion.domain.Publicacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comentario{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publicacion_id", nullable = false)
	private Publicacion publicacion;

	@JoinColumn(name = "usuario_id", nullable = false)
	private String autor;

	@Column(name = "contenido", nullable = false)
	private String contenido;

	@Column(name = "fecha_de_publicacion", nullable = false)
	private LocalDateTime fechaPublicacion;
}
