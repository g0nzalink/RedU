package com.example.backendredu.config;

import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

//Aqui se va a inicializar un admin por defecto

@Configuration
public class UserInitializer {
    @Bean
    public CommandLineRunner init(UsuarioRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@gmail.com");
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setUserType(Role.ADMINISTRADOR);
                admin.setDescription("Administrador del sistema");
                userRepository.save(admin);
                System.out.println("✔ Usuario ADMIN creado por defecto.");
            }
        };
    }
}
