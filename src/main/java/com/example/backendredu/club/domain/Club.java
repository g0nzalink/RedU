package com.example.backendredu.club.domain;

import com.example.backendredu.pertenece.domain.Pertenece;
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_names", nullable = false)
    private String lastNames;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_picture")
    private String profilePicture;

    @OneToMany(mappedBy = "club")
    private List<Pertenece> memberships;
}
