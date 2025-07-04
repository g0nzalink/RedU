package com.example.backendredu.pertenencia.domain;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.club.dto.ClubResumenDto;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PertenenciaService {

    private final PertenenciaRepository pertenenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClubRepository clubRepository;
    private final ModelMapper modelMapper;
    
    public Pertenencia crearPertenencia(String usuarioEmail, String clubEmail) {
        Usuario usuario = usuarioRepository.findById(usuarioEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Club club = clubRepository.findById(clubEmail)
                .orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));

        boolean yaExiste = pertenenciaRepository.existsByUsuarioIdAndClubIdAndRelacion(usuario, club, Relacion.SEGUIDOR);
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

    public List<Usuario> obtenerSeguidores(String clubEmail) {
        Club club = clubRepository.findById(clubEmail)
                .orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));

        List<Pertenencia> pertenencias = pertenenciaRepository
                .findByClubIdAndRelacion(club, Relacion.SEGUIDOR);

        return pertenencias.stream()
                .map(Pertenencia::getUsuarioId)
                .toList();
    }
    
    public List<Club> obtenerClubesSeguidos(String usuarioEmail) {
        Usuario usuario = usuarioRepository.findById(usuarioEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        
        List<Pertenencia> pertenencias = pertenenciaRepository
                .findByUsuarioIdAndRelacion(usuario, Relacion.SEGUIDOR);
        
        return pertenencias.stream()
                .map(Pertenencia::getClubId)
                .toList();
    }

    public Pertenencia designarDirectiva(String usuarioEmail, String clubEmail) {
        Usuario usu = usuarioRepository.findById(usuarioEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Club club = clubRepository.findById(clubEmail)
                .orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));

        if (pertenenciaRepository.existsByUsuarioIdAndClubIdAndRelacion(usu, club, Relacion.DIRECTIVA)) {
            throw new IllegalStateException("Ya es directiva de este club");
        }

        Pertenencia p = new Pertenencia();
        p.setUsuarioId(usu);
        p.setClubId(club);
        p.setFechaUnion(LocalDate.now());
        p.setRelacion(Relacion.DIRECTIVA);
        usu.setUserType(Role.DIRECTIVA);
        usuarioRepository.save(usu);
        return pertenenciaRepository.save(p);
    }

    public Pertenencia crearPertenenciaMiembro(
            String operadorEmail,
            String nuevoMiembroEmail,
            String clubEmail
    ) {
        clubRepository.findById(clubEmail)
                .orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));
        pertenenciaRepository.findByUsuarioIdEmailAndClubIdEmailAndRelacion(
                        operadorEmail, clubEmail, Relacion.DIRECTIVA)
                .orElseThrow(() -> new AccessDeniedException("No eres directiva de este club"));

        Usuario nuevo = usuarioRepository.findById(nuevoMiembroEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario a designar no encontrado"));
        Club club = clubRepository.findById(clubEmail).get();

        if (pertenenciaRepository.existsByUsuarioIdAndClubIdAndRelacion(nuevo, club, Relacion.MIEMBRO)) {
            throw new IllegalStateException("Ya es miembro de este club");
        }

        Pertenencia p = new Pertenencia();
        p.setUsuarioId(nuevo);
        p.setClubId(club);
        p.setFechaUnion(LocalDate.now());
        p.setRelacion(Relacion.MIEMBRO);
        return pertenenciaRepository.save(p);
    }

    public void eliminarRelacionDirectiva(String usuarioEmail, String clubEmail) {
        Pertenencia pertenencia = pertenenciaRepository
                .findByUsuarioIdEmailAndClubIdEmailAndRelacion(usuarioEmail, clubEmail, Relacion.DIRECTIVA)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la relación DIRECTIVA"));
        Usuario usuario = usuarioRepository.findById(usuarioEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario a designar no encontrado"));
        usuario.setUserType(Role.ALUMNO);
        pertenenciaRepository.delete(pertenencia);
    }

    public void eliminarRelacionMiembro(String operadorEmail, String usuarioEmail, String clubEmail) {
        pertenenciaRepository
                .findByUsuarioIdEmailAndClubIdEmailAndRelacion(operadorEmail, clubEmail, Relacion.DIRECTIVA)
                .orElseThrow(() -> new AccessDeniedException("No eres directiva de este club"));

        Pertenencia pertenencia = pertenenciaRepository
                .findByUsuarioIdEmailAndClubIdEmailAndRelacion(usuarioEmail, clubEmail, Relacion.MIEMBRO)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la relación MIEMBRO"));

        pertenenciaRepository.delete(pertenencia);
    }
    
    public boolean esDirectivaDeAlgunClub(String usuarioEmail) {
        List<Pertenencia> pertenencias = pertenenciaRepository.findAllByUsuarioIdEmailAndRelacion(usuarioEmail, Relacion.DIRECTIVA);
        System.out.println("Pertenencias encontradas para " + usuarioEmail + ": " + pertenencias.size());
        return !pertenencias.isEmpty();
    }
    
    public List<ClubResumenDto> getClubNombresComoDirectiva(String usuarioEmail) {
        List<Pertenencia> pertenencias = pertenenciaRepository.findAllByUsuarioIdEmailAndRelacion(usuarioEmail, Relacion.DIRECTIVA);
        List<ClubResumenDto> clubes = new ArrayList<>();
        for (Pertenencia pertenencia : pertenencias) {
            Club club = pertenencia.getClubId();
            clubes.add(new ClubResumenDto(club.getEmail(), club.getNombre()));
        }
        return clubes;
    }
    
    public List<ClubResumenDto> getClubesComoDirectiva(String usuarioEmail) {
        List<Pertenencia> pertenencias = pertenenciaRepository.findAllByUsuarioIdEmailAndRelacion(usuarioEmail, Relacion.DIRECTIVA);
        List<ClubResumenDto> clubes = new ArrayList<>();
        for (Pertenencia pertenencia : pertenencias) { clubes.add(modelMapper.map(pertenencia.getClubId(), ClubResumenDto.class)); }
        return clubes;
    }
    
    public Boolean esDirectivaDeClub(String clubEmail, String usuarioEmail) {
        Club club = clubRepository.findById(clubEmail).orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioEmail).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
		return pertenenciaRepository.existsByUsuarioIdAndClubIdAndRelacion(usuario, club, Relacion.DIRECTIVA);
    }
    
    public Boolean esMiembroDeClub(String clubEmail, String usuarioEmail) {
        Club club = clubRepository.findById(clubEmail).orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioEmail).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return pertenenciaRepository.existsByUsuarioIdAndClubIdAndRelacion(usuario, club, Relacion.MIEMBRO);
    }
}
