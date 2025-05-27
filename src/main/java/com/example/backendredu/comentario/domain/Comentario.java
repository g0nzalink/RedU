package com.example.backendredu.comentario.domain;

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

	@Column(name = "publicacion_de_origen", nullable = false)
	private Long publicacionId;

	@Column(name = "contenido", nullable = false)
	private String contenido;

	@Column(name = "fecha_de_publicacion", nullable = false)
	private LocalDateTime fechaPublicacion;
}
