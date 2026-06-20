package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "medecin")
@Data
@NoArgsConstructor
public class Medecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medecin")
    private Integer idMedecin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", unique = true)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etablissement")
    private Etablissement etablissement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_domaine", nullable = false)
    private DomaineMedical domaine;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 50)
    private String prenom;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "numero_ordre", unique = true, length = 50)
    private String numeroOrdre;

    @Column(name = "experience_ans")
    private Integer experienceAns;

    @Column(name = "tarif_consultation", precision = 10, scale = 2)
    private BigDecimal tarifConsultation;

    @Column(name = "note_moyenne", precision = 2, scale = 1)
    private BigDecimal noteMoyenne = BigDecimal.ZERO;

    @Column(name = "nb_avis")
    private Integer nbAvis = 0;

    @Column(name = "actif")
    private Boolean actif = true;
}
