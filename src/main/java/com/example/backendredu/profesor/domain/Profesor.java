package com.example.backendredu.profesor.domain;

import com.example.backendredu.usuario.domain.Role;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profesores")
@Data
@PrimaryKeyJoinColumn(name = "email")
public class Profesor extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "departmento", nullable = false)
    private Departamento departamento;
    
    @PrePersist
    public void asignarRol() {
        this.setUserType(Role.PROFESOR);
    }
}
