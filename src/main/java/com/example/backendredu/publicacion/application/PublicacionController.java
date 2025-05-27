package com.example.backendredu.publicacion.application;

import com.example.backendredu.comentario.domain.Comentario;
import com.example.backendredu.comentario.domain.ComentarioService;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.domain.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/publicacion")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;

    private final ComentarioService comentarioService;

    @PostMapping("/publicacion")
    public ResponseEntity<Publicacion> crearPublicacion(@RequestBody Publicacion publicacion){
        Publicacion createdPublicacion = publicacionService.createPublicacion(publicacion);
        return ResponseEntity.created(URI.create("http://localhost/publicacion/" + createdPublicacion.getId())).body(createdPublicacion);
    }

    @GetMapping
    public ResponseEntity<List<Publicacion>> listarPublicaciones() {
        List<Publicacion> publicaciones = publicacionService.allPublicaciones();
        return ResponseEntity.ok(publicaciones);
    }

    @GetMapping("/{idPublicacion}")
    public ResponseEntity<Publicacion> getPublicacion(@PathVariable("idPublicacion") Long idPub){
            return ResponseEntity.ok(publicacionService.getPublicacionById(idPub));
    }

    @PatchMapping("/actualizar/{publicacionId}")
    public ResponseEntity<Publicacion> actualizarPublicacion(@RequestBody Publicacion publicacion, @PathVariable Long publicacionId){
        Publicacion actualizado = publicacionService.actualizarPublicacion(publicacion, publicacionId);
        return ResponseEntity.ok(actualizado);
    }

    //Hasta aca son endpoints similares a los de proyecto, pero enfocados en publicacion

    //A partir de aca ya son los endpoints para hacer comentarios y tal

    @PatchMapping("/{publicacionId}/comentar")
    public ResponseEntity<Comentario> comentarPublicacion(@RequestBody Comentario comentario, @PathVariable Long publicacionId){
        Comentario creado = comentarioService.crearComentario(comentario, publicacionId);
        return ResponseEntity.created(URI.create("http://localhost/publicacion/" + creado.getId())).body(creado);
    }

    //Implementar metodo que obtenga la lista de los comentarios de la publicacion
    @GetMapping("/{publicacionId}/comentarios")
    public ResponseEntity<List<Comentario>> listarComentarios(@PathVariable Long publicacionId){
        return ResponseEntity.ok(comentarioService.listarComentarios(publicacionId));
    }

}

