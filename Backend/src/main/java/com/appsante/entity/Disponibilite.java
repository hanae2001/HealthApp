package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilite")
@Data
@NoArgsConstructor
public class Disponibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispo")
    private Integer idDispo;

    @Column(name = "id_medecin", nullable = false)
    private Integer idMedecin;

    @Column(name = "jour_semaine", nullable = false)
    private String jourSemaine;

    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;
}
