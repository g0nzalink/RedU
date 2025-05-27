package com.example.backendredu.club.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class ClubNotFoundException extends EntityNotFoundException {
	public ClubNotFoundException(String message) {
		super(message);
	}
}
