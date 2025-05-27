package com.example.backendredu.publicacion.domain;

import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.publicacion.exception.PublicacionNotFoundException;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PublicacionResponseDto createPublicacion(PublicacionRequestDto dto) {
        Publicacion entidad = modelMapper.map(dto, Publicacion.class);
        entidad.setEsProyecto(false);
        Publicacion saved = publicacionRepository.save(entidad);
        return modelMapper.map(saved, PublicacionResponseDto.class);
    }

    @Transactional
    public PublicacionResponseDto getPublicacionById(Long id) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));
        return modelMapper.map(p, PublicacionResponseDto.class);
    }

    @Transactional
    public List<PublicacionResponseDto> allPublicaciones() {
        return publicacionRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PublicacionResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PublicacionResponseDto actualizarPublicacion(PublicacionRequestDto newpublicacion, Long id) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));

        if (newpublicacion.getTitulo() != null) {
            publicacion.setTitulo(newpublicacion.getTitulo());
        }
        if (newpublicacion.getDescripcion() != null) {
            publicacion.setDescripcion(newpublicacion.getDescripcion());
        }
        if (newpublicacion.getFechaModificacion() != null) {
            publicacion.setFechaModificacion(newpublicacion.getFechaModificacion());
        }

        modelMapper.map(newpublicacion, publicacion);
        publicacion.setFechaModificacion(newpublicacion.getFechaModificacion());
        Publicacion updated = publicacionRepository.save(publicacion);
        return modelMapper.map(updated, PublicacionResponseDto.class);
    }
}

