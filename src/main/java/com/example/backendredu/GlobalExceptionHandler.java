package com.example.backendredu;


import com.example.backendredu.alumno.exceptions.AlumnoNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
 
  @ExceptionHandler(PublicacionNotFoundException.class)
  public ResponseEntity<String> handlePublicacionNotFoundException(PublicacionNotFoundException ex){
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleAlumnoNotFoundException(AlumnoNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

}
