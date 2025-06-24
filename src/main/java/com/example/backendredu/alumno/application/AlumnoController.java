package com.example.backendredu.alumno.application;

import com.example.backendredu.alumno.domain.AlumnoService;

import com.example.backendredu.alumno.dto.AlumnoRequestDto;
import com.example.backendredu.alumno.dto.AlumnoResponseDto;
import com.example.backendredu.alumno.email.AlumnoEmailEvent;
import com.example.backendredu.alumno.email.AlumnoEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alumno")
public class AlumnoController {

	private final AlumnoService alumnoService;
	
	private final ApplicationEventPublisher applicationEventPublisher;

	@GetMapping("/{email}")
	public ResponseEntity<AlumnoResponseDto> getAlumno(@PathVariable String email) {
		return ResponseEntity.ok(alumnoService.getAlumnoById(email));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody AlumnoRequestDto request) {
		alumnoService.register(request.getEmail(), request.getUsername(), request.getPassword(), request.getCarrera(), request.getFotoPerfilUrl());
		applicationEventPublisher.publishEvent(new AlumnoEmailEvent(new AlumnoEventDto(request.getEmail(), "Hola, " + request.getEmail() + "!", "Te has logueado correctamente a RedU! \n\n Si no fuiste tú, por favor responde a este correo sobre el problema y desactivaremos la cuenta. \n\n Gracias por unirte a RedU!")));
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado");
	}
}

