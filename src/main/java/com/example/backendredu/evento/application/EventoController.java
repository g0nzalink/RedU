package com.example.backendredu.evento.application;

import com.example.backendredu.evento.domain.EventoService;
import com.example.backendredu.evento.dto.EventoRequestDto;
import com.example.backendredu.evento.dto.EventoResponseDto;
import com.example.backendredu.publicacion.dto.PaginatedResponse;
import com.example.backendredu.usuario.dto.UsuarioAsistenteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/evento")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DIRECTIVA')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventoResponseDto> crearEvento(
            @RequestPart("data") EventoRequestDto eventoRequestDto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @AuthenticationPrincipal UserDetails userDetails) {

        EventoResponseDto creado = eventoService.crearEvento(eventoRequestDto, userDetails.getUsername(), imagen);

        return ResponseEntity
                .created(URI.create("http://localhost/evento/" + creado.getId()))
                .body(creado);
    }
    
    @GetMapping("/paged")
    public ResponseEntity<PaginatedResponse<EventoResponseDto>> listarEventosPaginados(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(eventoService.paginateEventos(username, page, limit));
    }
    
    @PostMapping("/solo-json")
    public ResponseEntity<EventoResponseDto> crearEventoJson(
            @RequestBody EventoRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(eventoService.crearEvento(dto, userDetails.getUsername(), null));
    }

    @GetMapping
    public ResponseEntity<List<EventoResponseDto>> listarEventos() {
        List<EventoResponseDto> eventos = eventoService.allEventos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{eventoId}")
    public ResponseEntity<EventoResponseDto> obtenerEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(eventoService.obtenerEvento(eventoId));
    }

    @PatchMapping(
            path = "/actualizar/{eventoId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EventoResponseDto> actualizarEvento(
            @RequestPart("data") EventoRequestDto eventoDto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @PathVariable Long eventoId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailLogeado = userDetails.getUsername();
        EventoResponseDto actualizado = eventoService.actualizarEvento(
                eventoDto, eventoId, emailLogeado, imagen
        );
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/{eventoId}/asistir")
    public ResponseEntity<Void> confirmarAsistencia(
            @PathVariable Long eventoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        eventoService.confirmarAsistencia(eventoId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventoId}/cancelar-asistencia")
    public ResponseEntity<Void> cancelarAsistencia(
            @PathVariable Long eventoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        eventoService.cancelarAsistencia(eventoId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventoId}/asistentes")
    public ResponseEntity<List<UsuarioAsistenteDto>> listarAsistentes(@PathVariable Long eventoId) {
        return ResponseEntity.ok(eventoService.obtenerAsistentesDto(eventoId));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DIRECTIVA')")
    @DeleteMapping("/{eventoId}")
    public ResponseEntity<Void> eliminarEvento(
            @PathVariable Long eventoId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String emailLogeado = userDetails.getUsername();
        eventoService.eliminarEvento(eventoId, emailLogeado);
        return ResponseEntity.noContent().build();
    }

}
