package com.example.backendredu.club.domain;

import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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
}
