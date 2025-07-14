package com.example.backendredu.evento.domain;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.proyecto.domain.Status;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "evento")
@PrimaryKeyJoinColumn(name = "id")
public class Evento extends Publicacion {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "club_email", referencedColumnName = "email")
    private Club club;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "lugar", nullable = false)
    private String lugar;

    @ManyToMany
    @JoinTable(
            name = "evento_asistentes",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_email")
    )
    private List<Usuario> asistentes = new ArrayList<>();

    @PrePersist
    public void esEvento(){
        this.setEsProyecto(false);
    }
}