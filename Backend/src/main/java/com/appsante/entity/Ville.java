package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ville")
@Data
@NoArgsConstructor
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ville")
    private Integer idVille;

    @Column(name = "nom_ville", nullable = false, length = 100)
    private String nomVille;

    @Column(name = "id_region", nullable = false)
    private Integer idRegion;
}
