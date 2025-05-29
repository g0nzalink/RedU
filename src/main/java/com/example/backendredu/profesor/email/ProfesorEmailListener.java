package com.example.backendredu.profesor.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfesorEmailListener {
	
	private final ProfesorEmailService profesorEmailService;
	
	@EventListener
	@Async
	public void handleProfesorEvent(ProfesorEmailEvent event) {
		profesorEmailService.sendEmail(event.getDto());
	}

}
