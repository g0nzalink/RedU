package com.example.backendredu.club.domain;

import com.example.backendredu.club.exceptions.ClubNotFoundException;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.pertenece.domain.Pertenece;
import com.example.backendredu.pertenece.domain.Relacion;
import com.example.backendredu.pertenece.infrastructure.PerteneceRepository;
import com.example.backendredu.usuario.domain.Rol;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    
    private final PerteneceRepository perteneceRepository;
    private final UsuarioRepository usuarioRepository;
    
    public Club createClub(Club club) {
        if (clubRepository.existsById(club.getEmail())) {
            throw new IllegalArgumentException("Club ya existe con correo" + club.getEmail());
        }
        clubRepository.save(club);
        return club;
    }
    
    public void deleteClub(String email) {
        if (clubRepository.existsById(email)) {
            throw new EntityNotFoundException("Club no existe con correo" + email);
        }
        clubRepository.deleteById(email);
    }
    
    public void clubChangeRole(String userEmail, String clubEmail, Relacion relacion) {
        if (clubRepository.existsById(clubEmail)) {
            throw new ClubNotFoundException("Club no existe con correo" + clubEmail);
        }
        if (usuarioRepository.existsById(userEmail)) {
            throw new EntityNotFoundException("Usuario no existe con correo" + userEmail);
        }
        Optional<Pertenece> members = perteneceRepository.findByUsuarioIdEmailAndClubIdEmail(userEmail, clubEmail);
        if (members.isEmpty()) {
            throw new EntityNotFoundException("No existe usuario de correo" + userEmail + "en el club de correo" + clubEmail);
        }
        
        Pertenece pertenece = members.get();
        pertenece.setRelacion(relacion);
        perteneceRepository.save(pertenece);
    }
}

