package com.example.backendredu.pertenencia.domain;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PertenenciaService {

    private final PertenenciaRepository pertenenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClubRepository clubRepository;

    public Pertenencia crearPertenencia(String usuarioEmail, String clubEmail) {
        Usuario usuario = usuarioRepository.findById(usuarioEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Club club = clubRepository.findById(clubEmail)
                .orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));

        // Validar que no exista ya una pertenencia igual
        boolean yaExiste = pertenenciaRepository.existsByUsuarioIdAndClubId(usuario, club);
        if (yaExiste) {
            throw new IllegalStateException("Ya existe una relación entre el usuario y el club");
        }

        Pertenencia pertenencia = new Pertenencia();
        pertenencia.setUsuarioId(usuario);
        pertenencia.setClubId(club);
        pertenencia.setFechaUnion(LocalDate.now());
        pertenencia.setRelacion(Relacion.SEGUIDOR);

        return pertenenciaRepository.save(pertenencia);
    }

    public void borrarPertenencia(String usuarioEmail, String clubEmail) {
        usuarioRepository.findById(usuarioEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        clubRepository.findById(clubEmail)
                .orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));

        Pertenencia pertenencia = pertenenciaRepository.findByUsuarioIdEmailAndClubIdEmail(usuarioEmail, clubEmail)
                .orElseThrow(() -> new EntityNotFoundException("Pertenencia no encontrado"));

        pertenenciaRepository.delete(pertenencia);
    }

}
