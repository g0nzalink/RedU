package com.example.backendredu.alumno.application;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.alumno.domain.AlumnoService;

import com.example.backendredu.alumno.dto.AlumnoRequestDto;
import com.example.backendredu.alumno.dto.AlumnoResponseDto;
import com.example.backendredu.alumno.email.AlumnoEmailEvent;
import com.example.backendredu.alumno.email.AlumnoEventDto;
import com.example.backendredu.auth.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
		alumnoService.register(request.getEmail(), request.getUsername(), request.getPassword(), request.getCarrera());
		applicationEventPublisher.publishEvent(new AlumnoEmailEvent(new AlumnoEventDto(request.getEmail(), "Hola", "Te has logueado correctamente!")));
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado");
	}

	/*
	@GetMapping("/{correo}/likes")
	ResponseEntity<List<Publicacion>> getAlumnoLikes(@PathVariable String correo) {
		List<Publicacion> likes = alumnoService.getAlumnoLikes(correo);
		return ResponseEntity.ok(likes);
	}
	
	@GetMapping("/{correo}/follows")
	ResponseEntity<List<Club>> getAlumnoFollows(@PathVariable String correo) {
		List<Club> follows = alumnoService.getAlumnoFollows(correo);
		return ResponseEntity.ok(follows);
	}
	
	@PatchMapping("/{correo}/desc")
	ResponseEntity<Alumno> updateAlumnDescription(@PathVariable String correo, @RequestBody String newDesc) {
		Alumno alumno = alumnoService.updateAlumnoDescription(correo, newDesc);
		return ResponseEntity.ok(alumno);
	}
*/
}

