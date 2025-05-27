package com.example.backendredu.alumno.application;

import com.example.backendredu.alumno.domain.Alumno;
import com.example.backendredu.alumno.domain.AlumnoService;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.publicacion.domain.Publicacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alumno")
public class AlumnoController {

	private final AlumnoService alumnoService;
	
	@GetMapping("/{correo}")
	public ResponseEntity<Alumno> getAlumnoById(@PathVariable String correo) {
		Alumno alumno = alumnoService.getAlumnoById(correo);
		return ResponseEntity.ok(alumno);
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

