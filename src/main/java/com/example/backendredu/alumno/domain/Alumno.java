package com.example.backendredu.alumno.domain;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.usuario.domain.Carrera;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.domain.Rol;
import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "alumnos")
@Data
public class Alumno extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "career", nullable = false)
    private Carrera career;

    @Column(name = "faculty", nullable = false)
    private String faculty;
    
    @OneToMany(mappedBy = "follows")
    private List<Club> follows;

    @ManyToMany
    @JoinTable(
            name = "AlumnoLikesPublicacion",
            joinColumns = @JoinColumn(name = "alumnoCorreo"),
            inverseJoinColumns = @JoinColumn(name = "publicacionId")
    )
    private List<Publicacion> likes;
    
    @PrePersist
    public void asignarRol() {
        this.setUserType(Rol.ALUMNO);
    }
}
