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
                club.setDescripcion("No deberías ver esto, porque este club no debería aparecer!");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_981347af50cd4ccf960211bfbb58b824~mv2.png/v1/fill/w_428,h_438,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/logo%20(4).png");
                clubRepository.save(club);
                System.out.println("✔ Club ADMINCLUB creado por defecto.");
            }
            
            if (clubRepository.findByNombre("CMA").isEmpty()) {
                Club club = new Club();
                club.setEmail("cma@utec.edu.pe");
                club.setNombre("CMA");
                club.setDescripcion("Una organización que fortalece habilidades matemáticas con asesorías competencias y colaboración entre estudiantes.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_981347af50cd4ccf960211bfbb58b824~mv2.png/v1/fill/w_428,h_438,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/logo%20(4).png");
                clubRepository.save(club);
                System.out.println("✔ Club CMA creado por defecto.");
            }
            
            if (clubRepository.findByNombre("CSL").isEmpty()) {
                Club club = new Club();
                club.setEmail("csl@utec.edu.pe");
                club.setNombre("CSL");
                club.setDescripcion("Una organización que promueve el uso y desarrollo de tecnologías abiertas.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_381538a0b1a14e50a5279efb5b42a010~mv2.jpg/v1/fill/w_303,h_276,al_c,q_80,usm_0.66_1.00_0.01,enc_avif,quality_auto/CSL%202024-2%20logo.jpg");
                clubRepository.save(club);
                System.out.println("✔ Club CSL creado por defecto.");
            }
            
            if (clubRepository.findByNombre("CPC").isEmpty()) {
                Club club = new Club();
                club.setEmail("cpc@utec.edu.pe");
                club.setNombre("CPC");
                club.setDescripcion("Una organización que apoya a estudiantes en su preparación para competencias de programación a nivel nacional e internacional.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_7d0e4d4c058844b698cb72e0f4755188~mv2.png/v1/fill/w_385,h_270,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/OFICIAL.png");
                clubRepository.save(club);
                System.out.println("✔ Club CPC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Fúsica").isEmpty()) {
                Club club = new Club();
                club.setEmail("fusica@utec.edu.pe");
                club.setNombre("Fúsica");
                club.setDescripcion("Una organización que fortalece la cultura musical en la comunidad con eventos y espacios de expresión artística.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_ee94e608a37f4f81910612887b7b1d8b~mv2.png/v1/fill/w_710,h_440,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_ee94e608a37f4f81910612887b7b1d8b~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club Fúsica creado por defecto.");
            }
            if (clubRepository.findByNombre("UTEC Gaming").isEmpty()) {
                Club club = new Club();
                club.setEmail("gaming@utec.edu.pe");
                club.setNombre("UTEC Gaming");
                club.setDescripcion("Una organización que revaloriza los videojuegos como herramienta de formación y socialización.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_7b4beae8f5dc4ababe547df3aa2dbfb3~mv2.png/v1/fill/w_688,h_484,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_7b4beae8f5dc4ababe547df3aa2dbfb3~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club UTEC Gaming creado por defecto.");
            }
            
            if (clubRepository.findByNombre("KON TEAM").isEmpty()) {
                Club club = new Club();
                club.setEmail("konteam@utec.edu.pe");
                club.setNombre("KON TEAM");
                club.setDescripcion("Somos una organización que diseña vehículos eléctricos como aporte a la movilidad sostenible.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_abfd8d6f861d40c2892b31fcd7b51a71~mv2.png/v1/fill/w_848,h_444,al_c,q_90,usm_0.66_1.00_0.01,enc_avif,quality_auto/KON%20Principal.png");
                clubRepository.save(club);
                System.out.println("✔ Club KON TEAM creado por defecto.");
            }
            
            if (clubRepository.findByNombre("IISE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("iise@utec.edu.pe");
                club.setNombre("IISE UTEC");
                club.setDescripcion("Una organización que difunde la Ingeniería Industrial mediante actividades académicas alineadas a líneas de investigación.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_eb3889c6df4a404f8bc05a81d18e6957~mv2.png/v1/fill/w_154,h_148,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_eb3889c6df4a404f8bc05a81d18e6957~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club IISE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("ASCE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("asce@utec.edu.pe");
                club.setNombre("ASCE UTEC");
                club.setDescripcion("Una organización que fomenta el crecimiento personal y profesional de estudiantes de Ingeniería Civil mediante ponencias, talleres e investigaciones.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_f0896ab17be942c4b36be17fe768f5b8~mv2.png/v1/fill/w_186,h_122,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_f0896ab17be942c4b36be17fe768f5b8~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club ASCE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("RUA nodo UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("rua@utec.edu.pe");
                club.setNombre("RUA nodo UTEC");
                club.setDescripcion("Organización que concientiza sobre problemáticas ambientales con proyectos, voluntariados y eventos que involucran a la comunidad UTEC y la sociedad limeña.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_cb996122a60c4ae994064b1082b7c09b~mv2.png/v1/fill/w_190,h_86,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_cb996122a60c4ae994064b1082b7c09b~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club RUA nodo UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("ASTM UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("astm@utec.edu.pe");
                club.setNombre("ASTM UTEC");
                club.setDescripcion("Una organización que difunde estándares internacionales en diversas áreas ingenieriles mediante eventos educativos.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_2b0b2fc33c6e44b19fe2858698130a63~mv2.png/v1/fill/w_186,h_186,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_2b0b2fc33c6e44b19fe2858698130a63~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club ASTM UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("AIChE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("aiche@utec.edu.pe");
                club.setNombre("AIChE UTEC");
                club.setDescripcion("Una organización que promueve la formación integral en Ingeniería Química mediante talleres, charlas y actividades profesionales.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_1a15d3272be3479c824d52082e102f6f~mv2.png/v1/fill/w_204,h_108,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_1a15d3272be3479c824d52082e102f6f~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club AIChE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("WIE UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("wie@utec.edu.pe");
                club.setNombre("WIE UTEC");
                club.setDescripcion("Una organización que impulsa el crecimiento de mujeres en ingeniería y ciencias mediante espacios de colaboración, desarrollo académico e investigación en STEM.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_6bd4f6bc568344ceb5d308f2384ad6d3~mv2.png/v1/fill/w_452,h_390,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/LOGO%20(1)%20(1)_edited.png");
                clubRepository.save(club);
                System.out.println("✔ Club WIE UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("CIIVIAL UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("ciivial@utec.edu.pe");
                club.setNombre("CIIVIAL UTEC");
                club.setDescripcion("Una organización que concientiza sobre la importancia del transporte y la vialidad, proponiendo soluciones desde un enfoque ingenieril y social.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_66cf6377ca764e55a0b8b432ccad76a8~mv2.png/v1/fill/w_328,h_322,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_66cf6377ca764e55a0b8b432ccad76a8~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club CIIVIAL UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("ASME UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("asme@utec.edu.pe");
                club.setNombre("ASME UTEC");
                club.setDescripcion("Impulsamos proyectos relacionados con minería, ambiente y tecnología, promoviendo el pensamiento crítico, la creatividad y el compromiso social.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_75e8fa74a1114400844e6019baaa58c1~mv2.png/v1/fill/w_154,h_108,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_75e8fa74a1114400844e6019baaa58c1~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club ASME UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Aerospace").isEmpty()) {
                Club club = new Club();
                club.setEmail("aerospace@utec.edu.pe");
                club.setNombre("Aerospace");
                club.setDescripcion("Una organización que desarrolla proyectos aeroespaciales y difunde avances del sector.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_4892e96b032b40e7acb86dd3752d77e8~mv2.png/v1/crop/x_25,y_29,w_731,h_732/fill/w_176,h_176,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_4892e96b032b40e7acb86dd3752d77e8~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club Aerospace creado por defecto.");
            }
            
            if (clubRepository.findByNombre("CAPS UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("asesoriascaps@utec.edu.pe");
                club.setNombre("CAPS UTEC");
                club.setDescripcion("Organización que brinda asesorías académicas personalizadas, alineadas con la malla curricular de UTEC.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_fb5ff8f60e094a4aa5a200d16a7b3cf5~mv2.png/v1/fill/w_186,h_186,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_fb5ff8f60e094a4aa5a200d16a7b3cf5~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club CAPS UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("UTEC Debate Society").isEmpty()) {
                Club club = new Club();
                club.setEmail("debatesociety@utec.edu.pe");
                club.setNombre("UTEC Debate Society");
                club.setDescripcion("Una organización que potencia habilidades blandas mediante debates sobre innovación y tecnología.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_4287747e29514f18aa46c10afacd73bd~mv2.png/v1/fill/w_1166,h_444,al_c,q_90,usm_0.66_1.00_0.01,enc_avif,quality_auto/Logo%20UDS-2.png");
                clubRepository.save(club);
                System.out.println("✔ Club UTEC Debate Society creado por defecto.");
            }
            
            if (clubRepository.findByNombre("SWE").isEmpty()) {
                Club club = new Club();
                club.setEmail("swe@utec.edu.pe");
                club.setNombre("Society of Women Engineers");
                club.setDescripcion("Una organización que promueve la inclusión y empoderamiento de mujeres en ingeniería y tecnología.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_67ef243373794ad585226a22dc588696~mv2.png/v1/fill/w_176,h_94,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_67ef243373794ad585226a22dc588696~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club SWE creado por defecto.");
            }
            
            if (clubRepository.findByNombre("GIIT ROBOTICS").isEmpty()) {
                Club club = new Club();
                club.setEmail("giit@utec.edu.pe");
                club.setNombre("GIIT ROBOTICS");
                club.setDescripcion("Una organización que desarrolla proyectos de robótica con impacto social, integrando estudiantes de distintas carreras.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_83da4c140a3340df8d2609be73732bbe~mv2.png/v1/fill/w_174,h_76,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_83da4c140a3340df8d2609be73732bbe~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club GIIT ROBOTICS creado por defecto.");
            }
            
            if (clubRepository.findByNombre("Role & Board Games UTEC").isEmpty()) {
                Club club = new Club();
                club.setEmail("rbg_utec@utec.edu.pe");
                club.setNombre("Role & Board Games UTEC");
                club.setDescripcion("Una organización que fomenta habilidades blandas a través de juegos de mesa y rol en un espacio inclusivo.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_7cfd3b9f53cf4cc48f96ff8ab4c6b3ff~mv2.png/v1/fill/w_202,h_132,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_7cfd3b9f53cf4cc48f96ff8ab4c6b3ff~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club Role & Board Games UTEC creado por defecto.");
            }
            
            if (clubRepository.findByNombre("UTEC ACM").isEmpty()) {
                Club club = new Club();
                club.setEmail("acm@utec.edu.pe");
                club.setNombre("UTEC ACM");
                club.setDescripcion("Capítulo ACM: fomento de la programación y ciencias de la computación.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_fc56612bd5ce4490be267d0f0f1f13f2~mv2.png/v1/fill/w_126,h_148,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_fc56612bd5ce4490be267d0f0f1f13f2~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club UTEC ACM creado por defecto.");
            }
            
            if (clubRepository.findByNombre("ANEIC").isEmpty()) {
                Club club = new Club();
                club.setEmail("aneic_utec@utec.edu.pe");
                club.setNombre("ANEIC");
                club.setDescripcion("Asociación de Negocios y Emprendimiento: talleres y mentorías.");
                club.setFotoUrl("https://static.wixstatic.com/media/2d543e_e5418b447d2541d58a4e9688c40f1e21~mv2.png/v1/fill/w_154,h_186,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/2d543e_e5418b447d2541d58a4e9688c40f1e21~mv2.png");
                clubRepository.save(club);
                System.out.println("✔ Club ANEIC creado por defecto.");
            }
        };
    }
}