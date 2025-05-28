package com.example.backendredu.alumno.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlumnoEventDto {
	private String to;
	
	private String subject;
	
	private String text;
}
