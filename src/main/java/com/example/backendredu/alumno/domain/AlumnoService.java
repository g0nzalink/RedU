package com.example.backendredu.alumno.domain;

import com.example.backendredu.alumno.infrastructure.AlumnoRepository;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {
	final private AlumnoRepository alumnoRepository;
	
	public Alumno getAlumnoById (String correo) {
		return alumnoRepository.findById(correo).orElseThrow(() -> new EntityNotFoundException("No existe un alumno con el correo " + correo));
	}
	
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
}

