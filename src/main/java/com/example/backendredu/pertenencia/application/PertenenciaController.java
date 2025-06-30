package com.example.backendredu.pertenencia.application;

import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.club.dto.ClubResumenDto;
import com.example.backendredu.pertenencia.domain.PertenenciaService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PertenenciaController {
	
	private final PertenenciaService pertenenciaService;
	
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
