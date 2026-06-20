package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "etablissement")
@Data
@NoArgsConstructor
public class Etablissement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etablissement")
    private Integer idEtablissement;

    @Column(name = "nom_etablissement", nullable = false, length = 150)
    private String nomEtablissement;

    @Column(name = "id_type", nullable = false)
    private Integer idType;

    @Column(name = "id_secteur", nullable = false)
    private Integer idSecteur;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "site_web", length = 255)
    private String siteWeb;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "actif")
    private Boolean actif = true;
}
