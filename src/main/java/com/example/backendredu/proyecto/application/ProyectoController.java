package com.example.backendredu.proyecto.application;

import com.example.backendredu.comentario.domain.Comentario;
import com.example.backendredu.proyecto.domain.Proyecto;
import com.example.backendredu.proyecto.domain.ProyectoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Proyecto> crearProyecto(@RequestBody Proyecto proyecto){
        Proyecto creado = proyectoService.crearProyecto(proyecto);
        return ResponseEntity.created(URI.create("http://localhost/proyecto/" + creado.getId())).body(creado);
    }

    //Implementar que un usuario pueda obtener una lista de publicaciones
    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        List<Proyecto> proyectos = proyectoService.allProyectos();
        return ResponseEntity.ok(proyectos);
    }

    //Implementar que un usuario pueda obtener una publicacion en especifico
    @GetMapping("/{proyectoId}")
    public ResponseEntity<Proyecto> obtenerProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(proyectoService.obtenerProyecto(proyectoId));
    }

    /*
    La implementacion de que alguien pueda comentar se hace en publicacion, ya que tanto publicacion como proyecto
    puede tener comentarios.
    @PatchMapping
    public ResponseEntity<Proyecto> comentarProyecto(@RequestBody Comentario comentario){

    }
*/

    //Implementar que el usuario creador pueda modificar el proyecto, incluye el modificar el status
    //Falta implementar una mejor logica con dtos
    @PatchMapping("/actualizar/{proyectoId}")
    public ResponseEntity<Proyecto> actualizarProyecto(@RequestBody Proyecto proyecto, @PathVariable Long proyectoId){
        Proyecto actualizado = proyectoService.actualizarProyecto(proyecto, proyectoId);
        return ResponseEntity.ok(actualizado);
    }

}
