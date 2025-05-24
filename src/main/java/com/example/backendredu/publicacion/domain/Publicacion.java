package com.example.backendredu.publicacion.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "publicaciones")
@Data
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publication_date", nullable = false)
    private ZonedDateTime publicationDate;

    @Column(name = "last_modification", nullable = false)
    private ZonedDateTime lastModification;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false)
    private Tag tag;

    @Column(name = "autor", nullable = false)
    private String autor;
}