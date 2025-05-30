package com.example.backendredu.config;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.infrastructure.ClubRepository;
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
                club.setEmail("cma@utec.edu.pe");
                club.setNombre("CMA");
                club.setDescripcion("Club de Matematicas Avanzadas");
                clubRepository.save(club);
                System.out.println("✔ Club CMA creado por defecto.");
            }

            if (clubRepository.findByNombre("CSL").isEmpty()) {
                Club club = new Club();
                club.setEmail("csl@utec.edu.pe");
                club.setNombre("CSL");
                club.setDescripcion("Club de Software Libre");
                clubRepository.save(club);
                System.out.println("✔ Club CSL creado por defecto.");
            }

            if (clubRepository.findByNombre("CPC").isEmpty()) {
                Club club = new Club();
                club.setEmail("cpc@utec.edu.pe");
                club.setNombre("CPC");
                club.setDescripcion("Club de Programacion Competitiva");
                clubRepository.save(club);
                System.out.println("✔ Club CPC creado por defecto.");
            }

            if (clubRepository.findByNombre("Fusica").isEmpty()) {
                Club club = new Club();
                club.setEmail("fusica@utec.edu.pe");
                club.setNombre("Fusica");
                club.setDescripcion("Nos dedicamos a difundir la cultura musical");
                clubRepository.save(club);
                System.out.println("✔ Club Fusica creado por defecto.");
            }

        };
    }
}
