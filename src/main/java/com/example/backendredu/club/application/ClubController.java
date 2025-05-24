package com.example.backendredu.club.application;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.domain.ClubService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    // ---- Controladores para utec admin -------
    // Controller para superadmin
    @PostMapping("")
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        Club created = clubService.createClub(club);
        return ResponseEntity.ok(created);
    }

    // Controller para superadmin
    @DeleteMapping("/{email}")
    public ResponseEntity<Club> deleteClub(@PathVariable String email) {
        clubService.deleteClub(email);
        return ResponseEntity.ok().build();
    }

    // ------- Controladores para la directiva -------
    /*
    // Controller para directiva
    @PostMapping("/{email}/posts")
    public ResponseEntity<?> createPost(@PathVariable String email) {
        return null;
    }
    */
    @GetMapping("/{email}/directive")
    public ResponseEntity<?> getDirective(@PathVariable String email) {
        return null;
    }

    @PostMapping("/{email}/directive/add")
    public ResponseEntity<?> addMember(@PathVariable String email) {
        return null;
    }

    @PostMapping("/{email}/directive/remove")
    public ResponseEntity<?> removeMember(@PathVariable String email) {
        return null;
    }

    // ¿Se debería poder solicitir ser miembro de un club o  se maneja en la vida real?

    // ------- Controladores para todos -------
    // Controller para todos
    @GetMapping("/{email}")
    public ResponseEntity<Club> getClub(@PathVariable String email) {
        return ResponseEntity.ok(clubService.getClub(email));
    }

    // Controller para todos
    @GetMapping("")
    public ResponseEntity<List<Club>> getClubs() {
        return ResponseEntity.ok(clubService.getClubs());
    }

    // Controller para todos
    @GetMapping("/{email}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable String email) {
        return null;
    }

    // Controller para todos
    @GetMapping("/{email}/posts")
    public ResponseEntity<?> getPosts(@PathVariable String email) {
        return null;
    }

    // Endpoint para ver publicaciones por nombre y tag.
    @GetMapping("/{email}/publicaciones")
    public ResponseEntity<?> getPublicaciones(@PathVariable String email, @RequestParam String tag, @RequestParam String nombre) {
        return null;
    }
    // ¿Seguir y dejar de seguir debería también ser endpoints?
    // ¿Debería estar el endpoint para publicar un post aquí o en publicaciones?
}

