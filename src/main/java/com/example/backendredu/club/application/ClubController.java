package com.example.backendredu.club.application;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.domain.ClubService;
import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.pertenencia.domain.Pertenencia;
import com.example.backendredu.pertenencia.domain.PertenenciaService;
import com.example.backendredu.usuario.domain.FileStorageService;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.dto.UsuarioResponseDto;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    private final PertenenciaService pertenenciaService;

    private final ModelMapper modelMapper;
    
    private final FileStorageService fileStorageService;

    @GetMapping("/{email}")
    public ResponseEntity<ClubResponseDto> getClub(@PathVariable String email) {
        return ResponseEntity.ok(clubService.getClub(email));
    }

    @GetMapping
    public ResponseEntity<List<Club>> listarClubs() {
        return ResponseEntity.ok(clubService.allClubs());
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'DIRECTIVA')")
    @PostMapping("/{clubId}/seguir")
    public ResponseEntity<Pertenencia> seguirClub(
            @PathVariable String clubId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String usuarioEmail = userDetails.getUsername();
        Pertenencia pertenencia = pertenenciaService.crearPertenencia(usuarioEmail, clubId);
        return ResponseEntity.created(URI.create("http://localhost/club/" + pertenencia.getId())).body(pertenencia);
    }

    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'DIRECTIVA')")
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

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DIRECTIVA')")
    @PostMapping("/{clubId}/designarDirectiva/{usuarioEmail}")
    public ResponseEntity<Pertenencia> designarDirectiva(
            @PathVariable String clubId,
            @PathVariable String usuarioEmail
    ) {
        Pertenencia p = pertenenciaService.designarDirectiva(usuarioEmail, clubId);
        return ResponseEntity
                .created(URI.create("/club/" + clubId + "/directiva/" + usuarioEmail))
                .body(p);
    }

    @PreAuthorize("hasRole('DIRECTIVA')")
    @PostMapping("/{clubId}/miembro/{nuevoEmail}")
    public ResponseEntity<Pertenencia> newMiembro(
            @PathVariable String clubId,
            @PathVariable("nuevoEmail") String nuevoMiembroEmail,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String operadorEmail = userDetails.getUsername();
        Pertenencia p = pertenenciaService
                .crearPertenenciaMiembro(operadorEmail, nuevoMiembroEmail, clubId);
        return ResponseEntity
                .created(URI.create("/club/" + clubId + "/miembro/" + nuevoMiembroEmail))
                .body(p);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{clubId}/directiva/{usuarioEmail}")
    public ResponseEntity<Void> eliminarDirectiva(
            @PathVariable String clubId,
            @PathVariable String usuarioEmail
    ) {
        pertenenciaService.eliminarRelacionDirectiva(usuarioEmail, clubId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('DIRECTIVA')")
    @DeleteMapping("/{clubId}/miembro/{usuarioEmail}")
    public ResponseEntity<Void> eliminarMiembro(
            @PathVariable String clubId,
            @PathVariable String usuarioEmail,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String operadorEmail = userDetails.getUsername();
        pertenenciaService.eliminarRelacionMiembro(operadorEmail, usuarioEmail, clubId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/favoritos/{usuarioEmail}")
    public ResponseEntity<List<ClubResponseDto>> getClubesFavoritos(@PathVariable String usuarioEmail) {
        List<Club> clubes = pertenenciaService.obtenerClubesSeguidos(usuarioEmail);
        List<ClubResponseDto> dtos = clubes.stream()
                .map(club -> modelMapper.map(club, ClubResponseDto.class))
                .toList();
        System.out.println("🎯 DTOs generados:");
        dtos.forEach(dto -> System.out.println(dto));
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping("/logo/{email}")
    public ResponseEntity<String> uploadClubLogo(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file) {
        try {
            fileStorageService.storeClubLogo(email, file);
            return ResponseEntity.ok("Logo del club subido correctamente");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al subir logo: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/logo/{email}")
    public ResponseEntity<Resource> getClubProfilePhoto(@PathVariable String email) throws IOException {
        Resource image = fileStorageService.loadClubProfilePhoto(email);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }
    
    @GetMapping("/vista")
    public ResponseEntity<List<ClubResponseDto>> listarClubsDTO() {
        List<ClubResponseDto> dtos = clubService.allClubs().stream()
                .map(club -> modelMapper.map(club, ClubResponseDto.class))
                .toList();
        return ResponseEntity.ok(dtos);
    }
}

