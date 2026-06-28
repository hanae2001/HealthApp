package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "note_clinique")
@Data
@NoArgsConstructor
public class NoteClinique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_note")
    private Integer idNote;

    @Column(name = "id_dossier", nullable = false)
    private Integer idDossier;

    @Column(name = "id_medecin", nullable = false)
    private Integer idMedecin;

    @Column(name = "id_rdv")
    private Integer idRdv;

    @Column(name = "date_note", insertable = false, updatable = false)
    private LocalDateTime dateNote;

    @Column(name = "diagnostic", columnDefinition = "TEXT")
    private String diagnostic;

    @Column(name = "plan_traitement", columnDefinition = "TEXT")
    private String planTraitement;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;
}
