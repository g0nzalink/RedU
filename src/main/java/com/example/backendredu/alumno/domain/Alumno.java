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
    @Column(name = "carrera", nullable = false)
    private Carrera carrera;

    @Column(name = "facultad", nullable = false)
    private String facultad;
    
    @PrePersist
    public void asignarRol() {
        this.setUserType(Rol.ALUMNO);
    }
}
