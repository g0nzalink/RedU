package com.example.backendredu.alumno.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlumnoEmailListener {
	
	private final AlumnoEmailService alumnoEmailService;
	
	@EventListener
	@Async
	public void handleAlumnoEvent(AlumnoEmailEvent event) {
		alumnoEmailService.sendEmail(event.getDto());
	}

}
