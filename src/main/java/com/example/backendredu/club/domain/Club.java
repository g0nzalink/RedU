package com.example.backendredu.club.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "clubs")
@Data
public class Club {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "foto_url")
    private String fotoUrl;
}
