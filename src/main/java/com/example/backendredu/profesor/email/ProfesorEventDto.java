package com.example.backendredu.profesor.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfesorEventDto {
	private String to;
	
	private String subject;
	
	private String text;
}
