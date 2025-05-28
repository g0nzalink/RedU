package com.example.backendredu.alumno.email;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlumnoEmailEvent extends ApplicationEvent {
	
	private AlumnoEventDto dto;
	
	public AlumnoEmailEvent(AlumnoEventDto dto) {
		super(dto);
		this.dto = dto;
	}
}
