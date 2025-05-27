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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_names", nullable = false)
    private String lastNames;

    @Column(name = "description")
    private String description;
/*
    Por el MVP ahora no nos enfocamos en las fotos de perfil
    @Column(name = "profile_picture")
    private String profilePicture;
*/
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private Rol userType;
    
}


