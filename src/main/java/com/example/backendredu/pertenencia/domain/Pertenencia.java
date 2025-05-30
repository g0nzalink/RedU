package com.example.backendredu.pertenencia.domain;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "pertenece")
@Data
public class Pertenencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private Usuario usuarioId;

    @ManyToOne
    @JoinColumn(name = "club_email", referencedColumnName = "email")
    private Club clubId;

    @Column(name = "join_date", nullable = false)
    private LocalDate fechaUnion;

    @Enumerated(EnumType.STRING)
    @Column(name = "relacion", nullable = false)
    private Relacion relacion;

}

