package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "dossier_medical")
@Data
@NoArgsConstructor
public class DossierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dossier")
    private Integer idDossier;

    @Column(name = "id_patient", nullable = false, unique = true)
    private Integer idPatient;

    @Column(name = "date_creation", insertable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(name = "antecedents_medicaux", columnDefinition = "TEXT")
    private String antecedentsMedicaux;

    @Column(name = "antecedents_dentaires", columnDefinition = "TEXT")
    private String antecedentsDentaires;

    @Column(name = "notes_generales", columnDefinition = "TEXT")
    private String notesGenerales;
}
