package com.example.backendredu;


import com.example.backendredu.alumno.exceptions.AlumnoNotFoundException;
import com.example.backendredu.club.exceptions.ClubNotFoundException;
import com.example.backendredu.exceptions.EmailAlreadyExistsException;
import com.example.backendredu.exceptions.UsernameAlreadyExistsException;
import com.example.backendredu.publicacion.exception.PublicacionNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(PublicacionNotFoundException.class)
	public ResponseEntity<String> handlePublicacionNotFoundException(PublicacionNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(AlumnoNotFoundException.class)
	public ResponseEntity<String> handleAlumnoNotFoundException(AlumnoNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(ClubNotFoundException.class)
	public ResponseEntity<String> handleClubNotFoundException(ClubNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}

	// Excepciones de registro :0
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public ResponseEntity<String> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}
