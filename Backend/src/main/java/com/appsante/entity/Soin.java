package com.appsante.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "soin")
@Data
@NoArgsConstructor
public class Soin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_soin")
    private Integer idSoin;

    @Column(name = "id_dossier", nullable = false)
    private Integer idDossier;

    @Column(name = "id_medecin", nullable = false)
    private Integer idMedecin;

    @Column(name = "id_rdv")
    private Integer idRdv;

    @Column(name = "type_soin", nullable = false, length = 100)
    private String typeSoin;

    @Column(name = "dent_concernee", length = 20)
    private String dentConcernee;

    @Column(name = "date_soin", nullable = false)
    private LocalDate dateSoin;

    @Column(name = "cout", nullable = false, precision = 10, scale = 2)
    private BigDecimal cout = BigDecimal.ZERO;

    @Column(name = "statut", length = 20)
    private String statut = "termine";
}
