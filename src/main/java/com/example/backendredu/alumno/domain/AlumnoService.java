package com.example.backendredu.alumno.domain;

import com.example.backendredu.alumno.infrastructure.AlumnoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

	final private AlumnoRepository alumnoRepository;
	
	public Alumno getAlumnoById (String correo) {
		return alumnoRepository.findById(correo).orElseThrow(() -> new EntityNotFoundException("No existe un alumno con el correo " + correo));
	}

	/*
	public List<Publicacion> getAlumnoLikes (String correo) {
		Alumno alumno = getAlumnoById(correo);
		return alumno.getLikes();
	}
	
	public List<Club> getAlumnoFollows (String correo) {
		Alumno alumno = getAlumnoById(correo);
		return alumno.getFollows();
	}
	
	public Alumno updateAlumnoDescription(String correo, String newDesc) {
		Alumno alumno = getAlumnoById(correo);
		alumno.setDescription(newDesc);
		return alumno;
	}

	 */
}

