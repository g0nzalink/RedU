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
            if (clubRepository.findByNombre("ADMINCLUB").isEmpty()) {
                Club club = new Club();
                club.setEmail("adminclub@utec.edu.pe");
                club.setNombre("ADMINCLUB");
                club.setDescripcion("Club para Admins");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_981347af50cd4ccf960211bfbb58b824~mv2.png/v1/fill/w_428,h_438,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/logo%20(4).png");
                clubRepository.save(club);
                System.out.println("✔ Club ADMINCLUB creado por defecto.");
            }
            
            if (clubRepository.findByNombre("CMA").isEmpty()) {
                Club club = new Club();
                club.setEmail("cma@utec.edu.pe");
                club.setNombre("CMA");
                club.setDescripcion("Club de Matematicas Avanzadas");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_981347af50cd4ccf960211bfbb58b824~mv2.png/v1/fill/w_428,h_438,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/logo%20(4).png");
                clubRepository.save(club);
                System.out.println("✔ Club CMA creado por defecto.");
            }

            if (clubRepository.findByNombre("CSL").isEmpty()) {
                Club club = new Club();
                club.setEmail("csl@utec.edu.pe");
                club.setNombre("CSL");
                club.setDescripcion("Club de Software Libre");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_381538a0b1a14e50a5279efb5b42a010~mv2.jpg/v1/fill/w_303,h_276,al_c,q_80,usm_0.66_1.00_0.01,enc_avif,quality_auto/CSL%202024-2%20logo.jpg");
                clubRepository.save(club);
                System.out.println("✔ Club CSL creado por defecto.");
            }

            if (clubRepository.findByNombre("CPC").isEmpty()) {
                Club club = new Club();
                club.setEmail("cpc@utec.edu.pe");
                club.setNombre("CPC");
                club.setDescripcion("Club de Programacion Competitiva");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_7d0e4d4c058844b698cb72e0f4755188~mv2.png/v1/fill/w_385,h_270,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/OFICIAL.png");
                clubRepository.save(club);
                System.out.println("✔ Club CPC creado por defecto.");
            }

            if (clubRepository.findByNombre("Fusica").isEmpty()) {
                Club club = new Club();
                club.setEmail("fusica@utec.edu.pe");
                club.setNombre("Fusica");
                club.setDescripcion("Nos dedicamos a difundir la cultura musical");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_ee94e608a37f4f81910612887b7b1d8b~mv2.png/v1/fill/w_710,h_440,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_ee94e608a37f4f81910612887b7b1d8b~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club Fusica creado por defecto.");
            }

        };
    }
}
