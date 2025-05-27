package com.example.backendredu.club.application;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.domain.ClubService;
import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.pertenencia.domain.Pertenencia;
import com.example.backendredu.pertenencia.domain.PertenenciaService;
import com.example.backendredu.pertenencia.dto.PertenenciaRequestDto;
import com.example.backendredu.publicacion.dto.PublicacionResponseDto;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.dto.UsuarioResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    private final PertenenciaService pertenenciaService;

    private final ModelMapper modelMapper;

    @GetMapping("/{email}")
    public ResponseEntity<ClubResponseDto> getClub(@PathVariable String email) {
        return ResponseEntity.ok(clubService.getClub(email));
    }

    @GetMapping
    public ResponseEntity<List<Club>> listarClubs() {
        return ResponseEntity.ok(clubService.allClubs());
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR')")
    @PostMapping("/{clubId}/seguir")
    public ResponseEntity<Pertenencia> seguirClub(
            @PathVariable String clubId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String usuarioEmail = userDetails.getUsername();
        Pertenencia pertenencia = pertenenciaService.crearPertenencia(usuarioEmail, clubId);
        return ResponseEntity.created(URI.create("http://localhost/club/" + pertenencia.getId())).body(pertenencia);
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR')")
    @DeleteMapping("/{clubId}/dejarSeguir")
    public ResponseEntity<Pertenencia> dejarDeSeguirClub(
            @PathVariable String clubId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String usuarioEmail = userDetails.getUsername();
        pertenenciaService.borrarPertenencia(usuarioEmail, clubId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{email}/seguidores")
    public ResponseEntity<List<UsuarioResponseDto>> getSeguidores(
            @PathVariable("email") String clubEmail
    ) {
        List<Usuario> usuarios = pertenenciaService.obtenerSeguidores(clubEmail);
        List<UsuarioResponseDto> dtos = usuarios.stream()
                .map(u -> modelMapper.map(u, UsuarioResponseDto.class))
                .toList();
        return ResponseEntity.ok(dtos);
    }
/*

    // ---- Controladores para utec admin -------
    // Controller para superadmin
    @PostMapping("")
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        Club created = clubService.createClub(club);
        return ResponseEntity.ok(created);
    }

    // Controller para superadmin
    @DeleteMapping("/{email}")
    public ResponseEntity<Club> deleteClub(@PathVariable String email) {
        clubService.deleteClub(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}/directive")
    public ResponseEntity<?> getDirective(@PathVariable String email) {
        return null;
    }

    @PostMapping("/{email}/directive/add")
    public ResponseEntity<?> addMember(@PathVariable String email) {
        return null;
    }

    @PostMapping("/{email}/directive/remove")
    public ResponseEntity<?> removeMember(@PathVariable String email) {
        return null;
    }

    // ¿Se debería poder solicitir ser miembro de un club o  se maneja en la vida real?

    // ------- Controladores para todos -------
    // Controller para todos


    // Controller para todos
    @GetMapping("")
    public ResponseEntity<List<Club>> getClubs() {
        return ResponseEntity.ok(clubService.getClubs());
    }

    // Controller para todos
    @GetMapping("/{email}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable String email) {
        return null;
    }

    // Controller para todos
    @GetMapping("/{email}/posts")
    public ResponseEntity<?> getPosts(@PathVariable String email) {
        return null;
    }

    // Endpoint para ver publicaciones por nombre y tag.
    @GetMapping("/{email}/publicaciones")
    public ResponseEntity<?> getPublicaciones(@PathVariable String email, @RequestParam String tag, @RequestParam String nombre) {
        return null;
    }
    // ¿Seguir y dejar de seguir debería también ser endpoints?
    // ¿Debería estar el endpoint para publicar un post aquí o en publicaciones?

 */
}

