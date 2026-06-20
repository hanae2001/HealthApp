package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "receptionniste")
@Data
@NoArgsConstructor
public class Receptionniste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receptionniste")
    private Integer idReceptionniste;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", unique = true)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etablissement", nullable = false)
    private Etablissement etablissement;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 50)
    private String prenom;

    @Column(name = "telephone", length = 20)
    private String telephone;
}
