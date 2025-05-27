package com.example.backendredu.proyecto.domain;

import com.example.backendredu.proyecto.dto.ProyectoRequestDto;
import com.example.backendredu.proyecto.dto.ProyectoResponseDto;
import com.example.backendredu.proyecto.infrastructure.ProyectoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public ProyectoResponseDto crearProyecto(ProyectoRequestDto proyectoDto){
        Proyecto proyecto = modelMapper.map(proyectoDto, Proyecto.class);

        Proyecto saved = proyectoRepository.save(proyecto);

        return modelMapper.map(saved, ProyectoResponseDto.class);
    }

    @Transactional
    public List<ProyectoResponseDto> allProyectos() {
        return proyectoRepository.findAll().stream()
                .map(p -> modelMapper.map(p, ProyectoResponseDto.class))
                .collect(Collectors.toList());
    }

    public ProyectoResponseDto obtenerProyecto(Long id) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe"));
        return modelMapper.map(p, ProyectoResponseDto.class);
    }

    @Transactional
    public ProyectoResponseDto actualizarProyecto(ProyectoRequestDto newproyecto, Long proyectoId, String emailLogeado) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado"));

        String emailAutor = proyecto.getAutor().getEmail();
        if (!emailAutor.equals(emailLogeado)) {
            throw new AccessDeniedException("Solo el autor puede actualizar esta publicación");
        }

        if(newproyecto.getStatus() != null){
            proyecto.setStatus(newproyecto.getStatus());
        }
        if (newproyecto.getTitulo() != null) {
            proyecto.setTitulo(newproyecto.getTitulo());
        }
        if (newproyecto.getDescripcion() != null) {
            proyecto.setDescripcion(newproyecto.getDescripcion());
        }
        if (newproyecto.getFechaModificacion() != null) {
            proyecto.setFechaModificacion(newproyecto.getFechaModificacion());
        }
        proyecto.setFechaModificacion(LocalDateTime.now());

        Proyecto updated = proyectoRepository.save(proyecto);
        return modelMapper.map(updated, ProyectoResponseDto.class);
    }
}