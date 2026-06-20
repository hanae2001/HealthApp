package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "secteur")
@Data
@NoArgsConstructor
public class Secteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_secteur")
    private Integer idSecteur;

    @Column(name = "nom_secteur", nullable = false, length = 100)
    private String nomSecteur;

    @Column(name = "id_ville", nullable = false)
    private Integer idVille;
}
