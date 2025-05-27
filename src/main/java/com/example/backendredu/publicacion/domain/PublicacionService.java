package com.example.backendredu.publicacion.domain;

import com.example.backendredu.publicacion.exception.PublicacionNotFoundException;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    public Publicacion getPublicacionById(Long id){
        return publicacionRepository.findById(id).
                orElseThrow(() -> new PublicacionNotFoundException("Publicacion no encontrada con el id: " + id));
    }

    public Publicacion createPublicacion(Publicacion publicacion){
        return publicacionRepository.save(publicacion);
    }

    @Transactional
    public List<Publicacion> allPublicaciones(){
        return publicacionRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    public Publicacion actualizarPublicacion(Publicacion newpublicacion, Long publicacionId){
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException("Publicacion no encontrado"));

        if (newpublicacion.getTitulo() != null) {
            publicacion.setTitulo(newpublicacion.getTitulo());
        }
        if (newpublicacion.getDescripcion() != null) {
            publicacion.setDescripcion(newpublicacion.getDescripcion());
        }
        if (newpublicacion.getFechaModificacion() != null) {
            publicacion.setFechaModificacion(newpublicacion.getFechaModificacion());
        }

        return publicacionRepository.save(publicacion);
    }
}

