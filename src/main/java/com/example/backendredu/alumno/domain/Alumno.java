package com.example.backendredu.alumno.domain;

import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.domain.Role;
import jakarta.persistence.*;
import lombok.Data;

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
        this.setUserType(Role.ALUMNO);
    }
}
