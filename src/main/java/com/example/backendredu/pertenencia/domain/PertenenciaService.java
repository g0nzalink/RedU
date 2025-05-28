package com.example.backendredu.pertenencia.domain;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
        return pertenenciaRepository.save(p);
    }

    public Pertenencia crearPertenenciaMiembro(
            String operadorEmail,
            String nuevoMiembroEmail,
            String clubEmail
    ) {
        // 1) Chequear que quien llama sea directiva
        clubRepository.findById(clubEmail)
                .orElseThrow(() -> new EntityNotFoundException("Club no encontrado"));
        pertenenciaRepository.findByUsuarioIdEmailAndClubIdEmailAndRelacion(
                        operadorEmail, clubEmail, Relacion.DIRECTIVA)
                .orElseThrow(() -> new AccessDeniedException("No eres directiva de este club"));

        // 2) Cargar usuario a designar
        Usuario nuevo = usuarioRepository.findById(nuevoMiembroEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario a designar no encontrado"));
        Club club = clubRepository.findById(clubEmail).get();

        // 3) Comprobar que no sea ya miembro o directiva
        if (pertenenciaRepository.existsByUsuarioIdAndClubIdAndRelacion(nuevo, club, Relacion.MIEMBRO)) {
            throw new IllegalStateException("Ya es miembro de este club");
        }

        // 4) Crear la pertenencia MIEMBRO
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

        pertenenciaRepository.delete(pertenencia);
    }

    public void eliminarRelacionMiembro(String operadorEmail, String usuarioEmail, String clubEmail) {
        // Verificar que el operador es directiva del club
        pertenenciaRepository
                .findByUsuarioIdEmailAndClubIdEmailAndRelacion(operadorEmail, clubEmail, Relacion.DIRECTIVA)
                .orElseThrow(() -> new AccessDeniedException("No eres directiva de este club"));

        Pertenencia pertenencia = pertenenciaRepository
                .findByUsuarioIdEmailAndClubIdEmailAndRelacion(usuarioEmail, clubEmail, Relacion.MIEMBRO)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la relación MIEMBRO"));

        pertenenciaRepository.delete(pertenencia);
    }
}
