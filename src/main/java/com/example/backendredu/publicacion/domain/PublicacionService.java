package com.example.backendredu.publicacion.domain;

import com.example.backendredu.publicacion.exception.PublicacionNotFoundException;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PublicacionService {
    private final PublicacionRepository publicacionRepository;

    public Publicacion getPublicacionById(Long id){
        return publicacionRepository.findById(id).
                orElseThrow(() -> new PublicacionNotFoundException("Publicacion no encontrada con el id: " + id));
    }

    public Publicacion createPublicacion(Publicacion publicacion){return publicacionRepository.save(publicacion);}

}

