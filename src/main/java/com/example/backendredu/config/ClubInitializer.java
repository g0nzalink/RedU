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
            if (clubRepository.findByNombre("UTEC Esports").isEmpty()) {
                Club club = new Club();
                club.setEmail("utecesports@utec.edu.pe");
                club.setNombre("UTEC Esports");
                club.setDescripcion("Equipo de deportes electrónicos de UTEC, participamos en torneos y fomentamos la cultura gamer.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/utecesports.png");
                clubRepository.save(club);
                System.out.println("✔ Club UTEC Esports creado por defecto.");
            }
            
            if (clubRepository.findByNombre("KON TEAM").isEmpty()) {
                Club club = new Club();
                club.setEmail("konteam@utec.edu.pe");
                club.setNombre("KON TEAM");
                club.setDescripcion("Club de robótica KON TEAM: diseñamos y competimos con robots autónomos.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/kon-team.png");
                clubRepository.save(club);
                System.out.println("✔ Club KON TEAM creado por defecto.");
            }
            
            if (clubRepository.findByNombre("IISE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("iiseutec@utec.edu.pe");
                club.setNombre("IISE UTEC");
                club.setDescripcion("Capítulo IISE de UTEC: gestión industrial y optimización de procesos.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/iise-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club IISE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Tenis de Mesa UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("tenisdemesautec@utec.edu.pe");
                club.setNombre("Tenis de Mesa UTEC");
                club.setDescripcion("Practica y competición de tenis de mesa abierta a toda la comunidad UTEC.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/tenis-de-mesa-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club Tenis de Mesa UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("ASCE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("asceutec@utec.edu.pe");
                club.setNombre("ASCE UTEC");
                club.setDescripcion("Capítulo ASCE: fomentamos el desarrollo de la ingeniería civil y proyectos estructurales.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/asce-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club ASCE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Club de Dibujo").isEmpty()) {
                Club club = new Club();
                club.setEmail("clubdedibujo@utec.edu.pe");
                club.setNombre("Club de Dibujo");
                club.setDescripcion("Espacio para explorar técnicas de dibujo, ilustración y arte gráfico.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/club-de-dibujo.png");
                clubRepository.save(club);
                System.out.println("✔ Club de Dibujo creado por defecto.");
            }
            
            if (clubRepository.findByNombre("UTEC Pride").isEmpty()) {
                Club club = new Club();
                club.setEmail("utecpride@utec.edu.pe");
                club.setNombre("UTEC Pride");
                club.setDescripcion("Comunidad LGBTIQA+ de UTEC, promovemos inclusión, respeto y actividades culturales.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/utec-pride.png");
                clubRepository.save(club);
                System.out.println("✔ Club UTEC Pride creado por defecto.");
            }
            
            if (clubRepository.findByNombre("EWB").isEmpty()) {
                Club club = new Club();
                club.setEmail("ewb@utec.edu.pe");
                club.setNombre("EWB");
                club.setDescripcion("Engineers Without Borders: proyectos de ingeniería para comunidades vulnerables.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/ewb.png");
                clubRepository.save(club);
                System.out.println("✔ Club EWB creado por defecto.");
            }
            
            if (clubRepository.findByNombre("RUA nodo UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("ruanodoutec@utec.edu.pe");
                club.setNombre("RUA nodo UTEC");
                club.setDescripcion("Red Universitaria de Apoyo: voluntariado y acción social en UTEC.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/rua-nodo-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club RUA nodo UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("ASTM UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("astmutec@utec.edu.pe");
                club.setNombre("ASTM UTEC");
                club.setDescripcion("Capítulo ASTM: normas y pruebas de materiales en ingeniería.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/astm-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club ASTM UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("SWE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("sweutec@utec.edu.pe");
                club.setNombre("SWE UTEC");
                club.setDescripcion("Sociedad de Mujeres en Ingeniería: mentoría, charlas y networking.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/swe-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club SWE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("AIChE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("aicheutec@utec.edu.pe");
                club.setNombre("AIChE UTEC");
                club.setDescripcion("Capítulo AIChE: química e ingeniería de procesos en la práctica profesional.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/aiche-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club AIChE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("IEEE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("ieeeutec@utec.edu.pe");
                club.setNombre("IEEE UTEC");
                club.setDescripcion("Capítulo IEEE: electrónica, comunicaciones y desarrollo tecnológico.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/ieee-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club IEEE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("JEONSA").isEmpty()) {
                Club club = new Club();
                club.setEmail("jeonsa@utec.edu.pe");
                club.setNombre("JEONSA");
                club.setDescripcion("Juventud Ecológica de Org. Sociales y Ambientales: iniciativas verdes.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/jeonsa.png");
                clubRepository.save(club);
                System.out.println("✔ Club JEONSA creado por defecto.");
            }
            
            if (clubRepository.findByNombre("CIIVIAL UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("ciivialutec@utec.edu.pe");
                club.setNombre("CIIVIAL UTEC");
                club.setDescripcion("Club de innovación cívica: tecnología para el cambio social.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/ciivial-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club CIIVIAL UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Utec Gaming").isEmpty()) {
                Club club = new Club();
                club.setEmail("utecgaming@utec.edu.pe");
                club.setNombre("Utec Gaming");
                club.setDescripcion("Comunidad gamer: torneos y eventos de videojuegos.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/utec-gaming.png");
                clubRepository.save(club);
                System.out.println("✔ Club Utec Gaming creado por defecto.");
            }
            
            if (clubRepository.findByNombre("ASME UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("asmeutec@utec.edu.pe");
                club.setNombre("ASME UTEC");
                club.setDescripcion("Capítulo ASME: mecánica, energías y diseño mecánico.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/asme-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club ASME UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("IAHE-Chapter UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("iahechapterutec@utec.edu.pe");
                club.setNombre("IAHE-Chapter UTEC");
                club.setDescripcion("Asociación de Ingeniería en Salud: innovación biomédica.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/iahe-chapter-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club IAHE-Chapter UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Aerospace").isEmpty()) {
                Club club = new Club();
                club.setEmail("aerospace@utec.edu.pe");
                club.setNombre("Aerospace");
                club.setDescripcion("Club de aeronáutica: proyectos de drones y aeromodelismo.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/aerospace.png");
                clubRepository.save(club);
                System.out.println("✔ Club Aerospace creado por defecto.");
            }
            
            if (clubRepository.findByNombre("CAPS UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("capsutec@utec.edu.pe");
                club.setNombre("CAPS UTEC");
                club.setDescripcion("Club de apoyo psicológico y salud mental.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/caps-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club CAPS UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("UTEC Debate Society").isEmpty()) {
                Club club = new Club();
                club.setEmail("utecdebatesociety@utec.edu.pe");
                club.setNombre("UTEC Debate Society");
                club.setDescripcion("Práctica de debate y oratoria, fomentamos el pensamiento crítico.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_4287747e29514f18aa46c10afacd73bd~mv2.png/v1/fill/w_1166,h_444,al_c,q_90,usm_0.66_1.00_0.01,enc_avif,quality_auto/Logo%20UDS-2.png");
                clubRepository.save(club);
                System.out.println("✔ Club UTEC Debate Society creado por defecto.");
            }
            
            if (clubRepository.findByNombre("WORKAY").isEmpty()) {
                Club club = new Club();
                club.setEmail("workay@utec.edu.pe");
                club.setNombre("WORKAY");
                club.setDescripcion("Plataforma de empleabilidad: talleres y networking profesional.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/workay.png");
                clubRepository.save(club);
                System.out.println("✔ Club WORKAY creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Meowtec").isEmpty()) {
                Club club = new Club();
                club.setEmail("meowtec@utec.edu.pe");
                club.setNombre("Meowtec");
                club.setDescripcion("Club de amantes de los felinos: adopción responsable y eventos.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/meowtec.png");
                clubRepository.save(club);
                System.out.println("✔ Club Meowtec creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Club de Artes Escénicas").isEmpty()) {
                Club club = new Club();
                club.setEmail("clubdeartesescenicas@utec.edu.pe");
                club.setNombre("Club de Artes Escénicas");
                club.setDescripcion("Teatro, danza y performance artística en UTEC.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/club-de-artes-escenicas.png");
                clubRepository.save(club);
                System.out.println("✔ Club de Artes Escénicas creado por defecto.");
            }
            
            if (clubRepository.findByNombre("SAE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("saeutec@utec.edu.pe");
                club.setNombre("SAE UTEC");
                club.setDescripcion("Sociedad de Automoción: diseño y competición de vehículos.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/sae-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club SAE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("AniTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("anitec@utec.edu.pe");
                club.setNombre("AniTEC");
                club.setDescripcion("Club de animación digital y producción audiovisual.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/anitec.png");
                clubRepository.save(club);
                System.out.println("✔ Club AniTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Tinku UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("tinkuutec@utec.edu.pe");
                club.setNombre("Tinku UTEC");
                club.setDescripcion("Promoción de la cultura andina a través de la danza y música.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/tinku-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club Tinku UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("GIIT ROBOTICS").isEmpty()) {
                Club club = new Club();
                club.setEmail("giitrobotics@utec.edu.pe");
                club.setNombre("GIIT ROBOTICS");
                club.setDescripcion("Grupo de Innovación y Tecnología: robótica y mecatrónica aplicada.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/giit-robotics.png");
                clubRepository.save(club);
                System.out.println("✔ Club GIIT ROBOTICS creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Role & Board Games UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("roleboardgamesutec@utec.edu.pe");
                club.setNombre("Role & Board Games UTEC");
                club.setDescripcion("Encuentros de juegos de mesa y rol para todos los gustos.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/role-board-games-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club Role & Board Games UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("UTEC ACM").isEmpty()) {
                Club club = new Club();
                club.setEmail("utecacm@utec.edu.pe");
                club.setNombre("UTEC ACM");
                club.setDescripcion("Capítulo ACM: fomento de la programación y ciencias de la computación.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/utec-acm.png");
                clubRepository.save(club);
                System.out.println("✔ Club UTEC ACM creado por defecto.");
            }
            
            if (clubRepository.findByNombre("ANEIC").isEmpty()) {
                Club club = new Club();
                club.setEmail("aneic@utec.edu.pe");
                club.setNombre("ANEIC");
                club.setDescripcion("Asociación de Negocios y Emprendimiento: talleres y mentorías.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/aneic.png");
                clubRepository.save(club);
                System.out.println("✔ Club ANEIC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("WER UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("werutec@utec.edu.pe");
                club.setNombre("WER UTEC");
                club.setDescripcion("Women in Engineering Resources: apoyo y desarrollo de liderazgo femenino.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/wer-utec.png");
                clubRepository.save(club);
                System.out.println("✔ Club WER UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Kuntur Aerodesing").isEmpty()) {
                Club club = new Club();
                club.setEmail("kunturaerodesing@utec.edu.pe");
                club.setNombre("Kuntur Aerodesing");
                club.setDescripcion("Club de diseño aeroespacial: proyectos de diseño y simulación.");
                club.setFotoUrl("https://bienestarestudiantil.utec.edu.pe/wp-content/uploads/kuntur-aerodesing.png");
                clubRepository.save(club);
                System.out.println("✔ Club Kuntur Aerodesing creado por defecto.");
            }
        };
    }
}