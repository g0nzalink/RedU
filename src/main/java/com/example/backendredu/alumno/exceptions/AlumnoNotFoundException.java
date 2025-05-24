package com.example.backendredu.alumno.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class AlumnoNotFoundException extends EntityNotFoundException {
	public AlumnoNotFoundException(String message) { super(message); }
}
