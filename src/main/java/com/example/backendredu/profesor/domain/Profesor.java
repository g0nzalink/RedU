package com.example.backendredu.profesor.domain;


import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "profesores")
public class Profesor extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    private Departamento facultades;
}
