package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "rendez_vous")
@Data
@NoArgsConstructor
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rdv")
    private Integer idRdv;

    @Column(name = "id_patient", nullable = false)
    private Integer idPatient;

    @Column(name = "id_medecin", nullable = false)
    private Integer idMedecin;

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @Column(name = "duree")
    private Integer duree = 30;

    @Column(name = "motif", length = 255)
    private String motif;

    @Column(name = "statut", length = 20)
    private String statut = "planifie";

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
