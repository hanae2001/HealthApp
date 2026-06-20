package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "domaine_medical")
@Data
@NoArgsConstructor
public class DomaineMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_domaine")
    private Integer idDomaine;

    @Column(name = "nom_domaine", nullable = false, length = 100)
    private String nomDomaine;

    @Column(name = "icone", length = 50)
    private String icone;

    @Column(name = "description")
    private String description;

    @Column(name = "actif")
    private Boolean actif = true;
}
