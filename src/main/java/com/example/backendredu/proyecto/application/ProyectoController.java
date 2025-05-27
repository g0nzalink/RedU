package com.example.backendredu.proyecto.application;

import com.example.backendredu.proyecto.domain.Proyecto;
import com.example.backendredu.proyecto.domain.ProyectoService;
import com.example.backendredu.proyecto.dto.ProyectoRequestDto;
import com.example.backendredu.proyecto.dto.ProyectoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/proyecto")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    //Actualmente la implementacion es basica, de ahi se cambiara a DTO y de ahi a una revision con spring security
    @PostMapping
    public ResponseEntity<ProyectoResponseDto> crearProyecto(@RequestBody ProyectoRequestDto proyectoDto){
        ProyectoResponseDto creado = proyectoService.crearProyecto(proyectoDto);
        return ResponseEntity.created(URI.create("http://localhost/proyecto/" + creado.getId())).body(creado);
    }

    //Implementar que un usuario pueda obtener una lista de publicaciones
    @GetMapping
    public ResponseEntity<List<ProyectoResponseDto>> listarProyectos() {
        List<ProyectoResponseDto> proyectos = proyectoService.allProyectos();
        return ResponseEntity.ok(proyectos);
    }

    //Implementar que un usuario pueda obtener una publicacion en especifico
    @GetMapping("/{proyectoId}")
    public ResponseEntity<ProyectoResponseDto> obtenerProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(proyectoService.obtenerProyecto(proyectoId));
    }

    //Implementar que el usuario creador pueda modificar el proyecto, incluye el modificar el status
    //Falta implementar una mejor logica con dtos
    @PatchMapping("/actualizar/{proyectoId}")
    public ResponseEntity<ProyectoResponseDto> actualizarProyecto(@RequestBody ProyectoRequestDto proyecto, @PathVariable Long proyectoId){
        ProyectoResponseDto actualizado = proyectoService.actualizarProyecto(proyecto, proyectoId);
        return ResponseEntity.ok(actualizado);
    }
}
