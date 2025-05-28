package com.example.backendredu.comentario.domain;

import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario autor;
*/
	@Column(name = "contenido", nullable = false)
	private String contenido;

	@Column(name = "fecha_de_publicacion", nullable = false)
	private LocalDateTime fechaPublicacion;
}
