package com.example.backendredu.proyecto.application;

import com.example.backendredu.proyecto.domain.ProyectoService;
import com.example.backendredu.proyecto.dto.ProyectoRequestDto;
import com.example.backendredu.proyecto.dto.ProyectoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/proyecto")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;
    
    @PostMapping
    public ResponseEntity<ProyectoResponseDto> crearProyecto(
            @RequestBody ProyectoRequestDto proyectoDto,
            @AuthenticationPrincipal UserDetails userDetails){
        System.out.println("📩 Recibida creación de proyecto de: " + userDetails.getUsername());
        ProyectoResponseDto creado = proyectoService.crearProyecto(proyectoDto, userDetails.getUsername());
        return ResponseEntity.created(URI.create("http://localhost/proyecto/" + creado.getId())).body(creado);
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

    @PatchMapping("/actualizar/{proyectoId}")
    public ResponseEntity<ProyectoResponseDto> actualizarProyecto(
            @RequestBody ProyectoRequestDto proyecto,
            @PathVariable Long proyectoId,
            @AuthenticationPrincipal UserDetails userDetails){
        String emailLogeado = userDetails.getUsername();
        ProyectoResponseDto actualizado = proyectoService.actualizarProyecto(proyecto, proyectoId, emailLogeado);
        return ResponseEntity.ok(actualizado);
    }
}
