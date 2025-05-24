package com.example.backendredu.club.domain;

import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;

    // Métodos para superadmin
    @PostMapping("")
    public Club createClub(Club club) {
        if(clubRepository.existsById(club.getEmail())) {
            throw new IllegalArgumentException("Ya existe un club con ese email: " + club.getEmail());
        }
        return clubRepository.save(club);
    }

    // Métodos para superadmin
    public void deleteClub(@PathVariable String email) {
        if(!clubRepository.existsById(email)) {
            throw new IllegalArgumentException("No existe un club con ese email: " + email);
        }
        clubRepository.deleteById(email);
    }

    // Métodos para superadmin
    public void updateClub(@PathVariable String email) {

    }

    // ------- Métodos para la directiva -------
    /*
    // Métodos para directiva
    @PostMapping("/{email}/posts")
    public ResponseEntity<?> createPost(@PathVariable String email) {
        return null;
    }

    public ResponseEntity<?> getDirective(@PathVariable String email) {
        return null;
    }

    public ResponseEntity<?> addMember(@PathVariable String email) {
    }


    public ResponseEntity<?> removeMember(@PathVariable String email) {
        return null;
    }
     */

    // ------- Métodos para todos -------
    // Métodos para todos
    public Club getClub(@PathVariable String email) {
        return clubRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Club no encontrado: " + email));
    }

    // Métodos para todos
    public List<Club> getClubs() {
        return clubRepository.findAll();
    }

    /*
    // Métodos para todos
    public List<Usuario> getFollowers(@PathVariable String email) {
    }

    */

    // Métodos para todos
    /*
    public List<publicacion> getPosts(@PathVariable String email) {
        Club club = getClub(email);

    }
    */

    // Endpoint para ver publicaciones por nombre y tag.
    /*
    public List<publicacion> getFilteredPosts(@PathVariable String email, @RequestParam String tag, @RequestParam String nombre) {
        return null;
    }
    */

}

