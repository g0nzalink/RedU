package com.example.backendredu.publicacion.domain;

import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.publicacion.dto.PublicacionUpdateDto;
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
    public PublicacionResponseDto actualizarPublicacion(PublicacionUpdateDto dto, Long id) {
        Publicacion entidad = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con id: " + id));

        // Solo actualizamos lo que venga en el DTO
        if (dto.getTitulo() != null)             entidad.setTitulo(dto.getTitulo());
        if (dto.getDescripcion() != null)        entidad.setDescripcion(dto.getDescripcion());
        if (dto.getFechaModificacion() != null)  entidad.setFechaModificacion(dto.getFechaModificacion());
        if (dto.getListTag() != null) {
            entidad.setListTag(dto.getListTag());
        }

        Publicacion updated = publicacionRepository.save(entidad);
        return modelMapper.map(updated, PublicacionResponseDto.class);
    }
}

