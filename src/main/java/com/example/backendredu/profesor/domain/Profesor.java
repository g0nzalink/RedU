package com.example.backendredu.profesor.domain;


import com.example.backendredu.usuario.domain.Rol;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "profesores")
public class Profesor extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "departmento", nullable = false)
    private Departamento departamento;
    
    @PrePersist
    public void asignarRol() {
        this.setUserType(Rol.PROFESOR);
    }
}
