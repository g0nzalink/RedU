package com.example.backendredu.pertenencia.application;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.club.dto.ClubResumenDto;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.pertenencia.domain.PertenenciaService;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PertenenciaController {
	
	private final PertenenciaService pertenenciaService;
	
	@GetMapping("/esdirectiva/{clubEmail}/{usuarioEmail}")
	public ResponseEntity<Boolean> esDirectivaDeClub(@PathVariable String clubEmail, @PathVariable String usuarioEmail) {
		Boolean es = pertenenciaService.esDirectivaDeClub(clubEmail, usuarioEmail);
		return ResponseEntity.ok(es);
	}
	
	@GetMapping("/esmiembro/{clubEmail}/{usuarioEmail}")
	public ResponseEntity<Boolean> esMiembroDeClub(@PathVariable String clubEmail, @PathVariable String usuarioEmail) {
		Boolean es = pertenenciaService.esMiembroDeClub(clubEmail, usuarioEmail);
		return ResponseEntity.ok(es);
	}
	
	@GetMapping("/esdirectiva")
	public ResponseEntity<Boolean> esDirectiva(@AuthenticationPrincipal UserDetails userDetails) {
		System.out.println("Accediendo a /esdirectiva con: " + userDetails.getUsername());
		boolean es = pertenenciaService.esDirectivaDeAlgunClub(userDetails.getUsername());
		return ResponseEntity.ok(es);
	}
	
	@GetMapping("/directivade")
	public ResponseEntity<List<ClubResumenDto>> esDirectivaDe(@AuthenticationPrincipal UserDetails userDetails) {
		List<ClubResumenDto> clubes = pertenenciaService.getClubesComoDirectiva(userDetails.getUsername());
		return ResponseEntity.ok(clubes);
	}
}
