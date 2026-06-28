package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medicament_ordonnance")
@Data
@NoArgsConstructor
public class MedicamentOrdonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_ordonnance", nullable = false)
    private Integer idOrdonnance;

    @Column(name = "nom_medicament", nullable = false, length = 100)
    private String nomMedicament;

    @Column(name = "dosage", length = 100)
    private String dosage;

    @Column(name = "frequence", length = 100)
    private String frequence;

    @Column(name = "duree", length = 100)
    private String duree;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;
}
