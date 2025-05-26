package com.example.backendredu.publicacion.application;

import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.domain.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/publicacion")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;

    //private final ModelMapper modelMapper;

    @GetMapping("/{idPublicacion}")
    public ResponseEntity<Publicacion> getPublicacion(@PathVariable("idPublicacion") Long idPub){
            return ResponseEntity.ok(publicacionService.getPublicacionById(idPub));
    }

    @PostMapping("/publicacion")
    public ResponseEntity<Publicacion> createPublicacion(@RequestBody Publicacion publicacion){
        Publicacion createdPublicacion = publicacionService.createPublicacion(publicacion);
        return ResponseEntity.created(URI.create("http://localhost/publicacion/" + createdPublicacion.getId())).body(createdPublicacion);
    }


}

