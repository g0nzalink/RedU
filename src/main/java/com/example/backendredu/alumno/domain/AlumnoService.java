package com.example.backendredu.alumno.domain;

import com.example.backendredu.alumno.dto.AlumnoResponseDto;
import com.example.backendredu.alumno.infrastructure.AlumnoRepository;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

	final private AlumnoRepository alumnoRepository;

	final private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder encoder;

	@Transactional
	public AlumnoResponseDto getAlumnoById(String email) {
		Alumno alumno = alumnoRepository.findById(email)
				.orElseThrow(() -> new EntityNotFoundException(
						"No existe un alumno con el correo " + email));
		return modelMapper.map(alumno, AlumnoResponseDto.class);
	}

	public void register(String email, String username, String password, Carrera carrera) {
		Alumno alumno = new Alumno();
		alumno.setEmail(email);
		alumno.setUsername(username);
		alumno.setPassword(encoder.encode(password));
		alumno.setUserType(Role.ALUMNO);
		alumno.setCarrera(carrera);
		alumnoRepository.save(alumno);
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

