package com.example.backendredu.directiva.domain;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.usuario.domain.Carrera;
import com.example.backendredu.usuario.domain.Rol;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "directivas")
public class Directiva extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "career", nullable = false)
    private Carrera career;

    @Column(name = "faculty", nullable = false)
    private String faculty;

    @ManyToOne
    @JoinColumn(name = "club_email", referencedColumnName = "email")
    private Club club;

    @PrePersist
    public void asignarRol() {
        this.setUserType(Rol.DIRECTIVA);
    }
}