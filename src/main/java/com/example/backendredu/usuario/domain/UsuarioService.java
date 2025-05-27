package com.example.backendredu.usuario.domain;

import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
	
	private final PublicacionRepository publicacionRepository;
	
	public Publicacion createPublicacion(Publicacion publicacion) {
		publicacionRepository.save(publicacion);
		return publicacion;
	}
}

