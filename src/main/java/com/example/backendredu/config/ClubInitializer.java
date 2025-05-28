package com.example.backendredu.config;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ClubInitializer {

    @Bean
    public CommandLineRunner initClub(ClubRepository clubRepository, PasswordEncoder encoder) {
        return args -> {
            if (clubRepository.findByNombre("CMA").isEmpty()) {
                Club club = new Club();
                club.setEmail("CMA@gmail.com");
                club.setNombre("CMA");
                club.setDescripcion("Club de Matematicas Avanzadas");
                clubRepository.save(club);
                System.out.println("✔ Club CMA creado por defecto.");
            }

            if (clubRepository.findByNombre("CSL").isEmpty()) {
                Club club = new Club();
                club.setEmail("CSL@gmail.com");
                club.setNombre("CSL");
                club.setDescripcion("Club de Software Libre");
                clubRepository.save(club);
                System.out.println("✔ Club CSL creado por defecto.");
            }

        };
    }
}
