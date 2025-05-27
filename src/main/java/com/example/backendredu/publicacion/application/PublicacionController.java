package com.example.backendredu.publicacion.application;

import com.example.backendredu.comentario.domain.Comentario;
import com.example.backendredu.comentario.domain.ComentarioService;
import com.example.backendredu.comentario.dto.ComentarioRequestDto;
import com.example.backendredu.comentario.dto.ComentarioResponseDto;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.domain.PublicacionService;
import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import jakarta.validation.Valid;
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
    public ResponseEntity<PublicacionResponseDto> crearPublicacion(@RequestBody PublicacionRequestDto publicacion){
        PublicacionResponseDto createdPublicacion = publicacionService.createPublicacion(publicacion);
        return ResponseEntity.created(URI.create("http://localhost/publicacion/" + createdPublicacion.getId())).body(createdPublicacion);
    }

    @GetMapping
    public ResponseEntity<List<PublicacionResponseDto>> listarPublicaciones() {
        return ResponseEntity.ok(publicacionService.allPublicaciones());
    }

    @GetMapping("/{idPublicacion}")
    public ResponseEntity<PublicacionResponseDto> getPublicacion(@PathVariable Long idPublicacion) {
            return ResponseEntity.ok(publicacionService.getPublicacionById(idPublicacion));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PublicacionResponseDto> actualizarPublicacion(@PathVariable Long id, @Valid @RequestBody PublicacionRequestDto publicacion) {
        return ResponseEntity.ok(publicacionService.actualizarPublicacion(publicacion, id));
    }

    //Hasta aca son endpoints similares a los de proyecto, pero enfocados en publicacion

    //A partir de aca ya son los endpoints para hacer comentarios y tal

    @PatchMapping("/{publicacionId}/comentar")
    public ResponseEntity<ComentarioResponseDto> comentarPublicacion(@RequestBody ComentarioRequestDto comentario, @PathVariable Long publicacionId){
        ComentarioResponseDto creado = comentarioService.crearComentario(comentario, publicacionId);
        return ResponseEntity.created(URI.create("http://localhost/publicacion/" + creado.getId())).body(creado);
    }

    //Implementar metodo que obtenga la lista de los comentarios de la publicacion
    @GetMapping("/{publicacionId}/comentarios")
    public ResponseEntity<List<ComentarioResponseDto>> listarComentarios(@PathVariable Long publicacionId){
        return ResponseEntity.ok(comentarioService.listarComentarios(publicacionId));
    }

}

