package com.example.backendredu.proyecto.application;

import com.example.backendredu.proyecto.domain.ProyectoService;
import com.example.backendredu.proyecto.dto.ProyectoRequestDto;
import com.example.backendredu.proyecto.dto.ProyectoResponseDto;
import com.example.backendredu.publicacion.dto.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/proyecto")
@RequiredArgsConstructor
public class ProyectoController {
    
    private final ProyectoService proyectoService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProyectoResponseDto> crearProyecto(
            @RequestPart("data") ProyectoRequestDto proyectoDto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ProyectoResponseDto creado = proyectoService.crearProyecto(proyectoDto, userDetails.getUsername(), imagen);
        return ResponseEntity
                .created(URI.create("http://localhost/proyecto/" + creado.getId()))
                .body(creado);
    }
    
    @GetMapping("/paged")
    public ResponseEntity<PaginatedResponse<ProyectoResponseDto>> listarProyectosPaginados(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(proyectoService.paginateProyectos(username, page, limit));
    }
    
    @GetMapping
    public ResponseEntity<List<ProyectoResponseDto>> listarProyectos() {
        List<ProyectoResponseDto> proyectos = proyectoService.allProyectos();
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/{proyectoId}")
    public ResponseEntity<ProyectoResponseDto> obtenerProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(proyectoService.obtenerProyecto(proyectoId));
    }

    @PatchMapping(
            path = "/actualizar/{proyectoId}",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ProyectoResponseDto> actualizarProyecto(
            @RequestPart("data") ProyectoRequestDto proyectoDto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @PathVariable Long proyectoId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailLogeado = userDetails.getUsername();
        ProyectoResponseDto actualizado = proyectoService.actualizarProyecto(
                proyectoDto, proyectoId, emailLogeado, imagen
        );
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{proyectoId}")
    public ResponseEntity<Void> eliminarProyecto(
            @PathVariable Long proyectoId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String emailLogeado = userDetails.getUsername();
        proyectoService.eliminarProyecto(proyectoId, emailLogeado);
        return ResponseEntity.noContent().build();
    }
}
