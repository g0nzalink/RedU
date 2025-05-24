package com.example.backendredu.proyecto.domain;

import com.example.backendredu.profesor.domain.Departamento;
import com.example.backendredu.publicacion.domain.Publicacion;
import jakarta.persistence.*;

@Entity
@Table(name = "proyectos")
public class Proyecto extends Publicacion {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

}
