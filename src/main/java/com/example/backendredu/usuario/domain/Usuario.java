package com.example.backendredu.usuario.domain;

import com.example.backendredu.pertenece.domain.Pertenece;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @Column(name = "profile_picture")
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private Rol userType;
    
}


