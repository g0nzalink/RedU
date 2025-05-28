package com.example.backendredu.profesor.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfesorEmailService {
	
	private final JavaMailSenderImpl mailSender;
	
	public void sendEmail(ProfesorEventDto dto) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(dto.getTo());
		message.setSubject(dto.getSubject());
		message.setText(dto.getText());
		mailSender.send(message);
	}
}
