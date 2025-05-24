package com.example.backendredu.publicacion.domain;

import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicacionService {
    private final PublicacionRepository publicacionRepository;

    public Publicacion getPublicacionById(Long id){
        return publicacionRepository.findById(id).get();
    }

    public Publicacion createPublicacion(Publicacion publicacion){
        return publicacionRepository.save(publicacion);
    }

}

