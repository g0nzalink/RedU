package com.example.backendredu.proyecto.domain;

import com.example.backendredu.profesor.domain.Departamento;
import com.example.backendredu.publicacion.domain.Publicacion;
import jakarta.persistence.*;

@Entity
public class Proyecto extends Publicacion {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "cantidad_de_participantes", nullable = false)
    private Integer capacidad;

    @PrePersist
    public void esProyecto(){
        this.status=Status.ACTIVO;
        this.setEsProyecto(true);
    }

}
