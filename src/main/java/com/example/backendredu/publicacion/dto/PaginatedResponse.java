package com.example.backendredu.publicacion.dto;

import java.util.List;

public class PaginatedResponse<T> {
	private List<T> publicaciones;
	private boolean hasNext;
	
	public PaginatedResponse(List<T> publicaciones, boolean hasNext) {
		this.publicaciones = publicaciones;
		this.hasNext = hasNext;
	}
	
	public List<T> getPublicaciones() { return publicaciones; }
	
	public boolean isHasNext() { return hasNext; }
}
