package com.appsante.entity;

import jakarta.persistence.*;//Toutes les annotations JPA (@Entity, @Table, @Id, @Column...)
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient")
    private Integer idPatient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", unique = true)
    private Utilisateur utilisateur;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 50)
    private String prenom;

    // Email dédié pour les rappels RDV (distinct de utilisateur.email)
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "groupe_sanguin", length = 5)
    private String groupeSanguin;

    @Column(name = "allergies")
    private String allergies;

    // Géré par MySQL DEFAULT (CURRENT_DATE)
    @Column(name = "date_inscription", insertable = false, updatable = false)
    private LocalDate dateInscription;
}
