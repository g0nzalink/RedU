package com.example.backendredu.usuario.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Usuario {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "contrasenia")
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private Rol userType;
    
}


