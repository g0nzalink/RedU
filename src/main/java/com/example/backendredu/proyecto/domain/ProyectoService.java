package com.example.backendredu.proyecto.domain;

import com.example.backendredu.proyecto.infrastructure.ProyectoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    @Transactional
    public Proyecto crearProyecto(Proyecto proyecto){
        return proyectoRepository.save(proyecto);
    }

    @Transactional
    public List<Proyecto> allProyectos(){
        return proyectoRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    public Proyecto obtenerProyecto(Long proyectoId){
        return proyectoRepository.findById(proyectoId).get();
    }

    public Proyecto actualizarProyecto(Proyecto newproyecto, Long proyectoId){
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado"));

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

        return proyectoRepository.save(proyecto);
    }




}
