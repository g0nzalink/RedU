package com.example.backendredu.publicacion.application;

import aj.org.objectweb.asm.TypeReference;
import com.example.backendredu.comentario.domain.ComentarioService;
import com.example.backendredu.comentario.dto.ComentarioRequestDto;
import com.example.backendredu.comentario.dto.ComentarioResponseDto;
import com.example.backendredu.publicacion.domain.PublicacionService;
import com.example.backendredu.publicacion.dto.PublicacionRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.publicacion.dto.PublicacionUpdateDto;
import com.example.backendredu.usuario.dto.UsuarioResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/publicacion")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;

    private final ComentarioService comentarioService;

    @GetMapping
    public ResponseEntity<List<PublicacionResponseDto>> listarPublicaciones(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(publicacionService.allPublicaciones(userDetails.getUsername()));
    }
    
    @GetMapping("/paged")
    public ResponseEntity<?> listarPublicacionesPaginadas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        return ResponseEntity.ok(publicacionService.paginatePublicaciones(userDetails.getUsername(), page, limit));
    }

    @GetMapping("/{idPublicacion}")
    public ResponseEntity<PublicacionResponseDto> getPublicacion(@PathVariable Long idPublicacion) {
            return ResponseEntity.ok(publicacionService.getPublicacionById(idPublicacion));
    }

    @PatchMapping("/{publicacionId}/comentar")
    public ResponseEntity<ComentarioResponseDto> comentarPublicacion(
            @RequestBody ComentarioRequestDto comentario,
            @PathVariable Long publicacionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        ComentarioResponseDto creado = comentarioService.crearComentario(userDetails.getUsername(), comentario, publicacionId);
        return ResponseEntity.created(URI.create("http://localhost/publicacion/" + creado.getId())).body(creado);
    }

    @GetMapping("/{publicacionId}/comentarios")
    public ResponseEntity<List<ComentarioResponseDto>> listarComentarios(@PathVariable Long publicacionId){
        return ResponseEntity.ok(comentarioService.listarComentarios(publicacionId));
    }

    //darle like a una publicacion
    @PatchMapping("/{publicacionId}/like")
    public ResponseEntity<PublicacionResponseDto> likePublicacion(@PathVariable Long publicacionId,
                                                                  @AuthenticationPrincipal UserDetails userDetails){
        PublicacionResponseDto resp = publicacionService.newLike(publicacionId, userDetails.getUsername());
        return ResponseEntity.ok(resp);
    }

    //ver usuarios que le dieron like a una publicacion
    @GetMapping("/{publicacionId}/seeLikes")
    public ResponseEntity<List<UsuarioResponseDto>> verLikes(@PathVariable Long publicacionId){
        return ResponseEntity.ok(publicacionService.getUserLikes(publicacionId));
    }
    
    @GetMapping("/{publicacionId}/likedByMe")
    public ResponseEntity<Boolean> likedByCurrentUser(@PathVariable Long publicacionId,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        boolean liked = publicacionService.wasLikedByUser(publicacionId, userDetails.getUsername());
        return ResponseEntity.ok(liked);
    }
    
    @PostMapping("/{idPublicacion}/upload")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DIRECTIVA')")
    public ResponseEntity<String> uploadPostPhoto(
            @PathVariable Long idPublicacion,
            @RequestParam MultipartFile imagen,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        String url = publicacionService.subirImagen(idPublicacion, userDetails.getUsername(), imagen);
        return ResponseEntity.ok(url);
    }
}

