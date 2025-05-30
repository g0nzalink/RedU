package com.example.backendredu.proyecto.domain;

import com.example.backendredu.publicacion.domain.Publicacion;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "proyecto")
@PrimaryKeyJoinColumn(name = "id")
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
