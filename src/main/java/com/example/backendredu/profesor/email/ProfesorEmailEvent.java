package com.example.backendredu.profesor.email;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProfesorEmailEvent extends ApplicationEvent {
	
	private ProfesorEventDto dto;
	
	public ProfesorEmailEvent(ProfesorEventDto dto) {
		super(dto);
		this.dto = dto;
	}
}
