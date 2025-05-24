package com.example.backendredu.pertenece.domain;


import com.example.backendredu.club.domain.Club;
import com.example.backendredu.proyecto.domain.Status;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "pertenece")
@Data
public class Pertenece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private Usuario user;

    @ManyToOne
    @JoinColumn(name = "club_email", referencedColumnName = "email")
    private Club club;

    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "relaciion", nullable = false)
    private Relacion relaciion;

}

