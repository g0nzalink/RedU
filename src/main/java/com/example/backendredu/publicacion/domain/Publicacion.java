package com.example.backendredu.publicacion.domain;


import com.example.backendredu.comentario.domain.Comentario;
import com.example.backendredu.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publicaciones")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_email", referencedColumnName = "email", nullable = false)
    private Usuario autor;
    
    @Column(name = "fechaPublicacion", nullable = false)
    private LocalDateTime fechaPublicacion;
    
    @Column(name = "fechaModificacion", nullable = false)
    private LocalDateTime fechaModificacion;
    
    @Column(name = "titulo", nullable = false)
    private String titulo;
    
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> listComentario = new ArrayList<>();
    
    @Convert(converter = TagListConverter.class)
    @Column(name = "listTag", nullable = false)
    private List<Tag> listTag;

    @Column
    private Boolean esProyecto;
}