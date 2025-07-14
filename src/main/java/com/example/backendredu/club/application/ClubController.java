package com.example.backendredu.club.application;

import com.example.backendredu.cloudinary.CloudinaryService;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.domain.ClubService;
import com.example.backendredu.club.dto.ClubCreateDto;
import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.club.exceptions.ClubNotFoundException;
import com.example.backendredu.club.infrastructure.ClubRepository;
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
    
    private final CloudinaryService cloudinaryService;

    private final PertenenciaService pertenenciaService;

    private final ModelMapper modelMapper;
    
    private final ClubRepository clubRepository;

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

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DIRECTIVA')")
    @DeleteMapping("/{clubId}/directiva/{usuarioEmail}")
    public ResponseEntity<Void> eliminarDirectiva(
            @PathVariable String clubId,
            @PathVariable String usuarioEmail
    ) {
        pertenenciaService.eliminarRelacionDirectiva(usuarioEmail, clubId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DIRECTIVA')")
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
        dtos.forEach(dto -> System.out.println(dto));
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/vista")
    public ResponseEntity<List<ClubResponseDto>> listarClubsDTO() {
        List<ClubResponseDto> dtos = clubService.allClubs().stream()
                .map(club -> modelMapper.map(club, ClubResponseDto.class))
                .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DIRECTIVA')")
    @PostMapping("/logo/{clubEmail}")
    public ResponseEntity<String> uploadClubLogo(
            @PathVariable String clubEmail,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            boolean esAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));
            boolean esDirectiva = pertenenciaService.esDirectivaDeClub(clubEmail, userDetails.getUsername());
            
            if (!esAdmin && !esDirectiva) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permisos para modificar el logo de este club.");
            }
            
            Club club = clubRepository.findById(clubEmail)
                    .orElseThrow(() -> new ClubNotFoundException("Club no encontrado."));
            System.out.println("✅ Club encontrado: " + club.getNombre());
            
            String url = cloudinaryService.uploadImage(file, "clubs", clubEmail);
            club.setFotoUrl(url);
            clubRepository.save(club);
            
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ClubResponseDto> crearClub(@RequestBody ClubCreateDto clubCreateDto) {
        ClubResponseDto club = clubService.crearClub(clubCreateDto);
        return ResponseEntity.created(URI.create("/club/" + club.getEmail())).body(club);
    }
}

