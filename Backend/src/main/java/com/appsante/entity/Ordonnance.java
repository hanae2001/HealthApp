package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "ordonnance")
@Data
@NoArgsConstructor
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ordonnance")
    private Integer idOrdonnance;

    @Column(name = "id_dossier", nullable = false)
    private Integer idDossier;

    @Column(name = "id_medecin", nullable = false)
    private Integer idMedecin;

    @Column(name = "id_rdv")
    private Integer idRdv;

    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;

    @Column(name = "validite_jours")
    private Integer validiteJours = 30;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
