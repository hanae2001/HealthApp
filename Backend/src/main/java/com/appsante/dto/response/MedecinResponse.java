package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MedecinResponse {
    private Integer idMedecin;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String numeroOrdre;
    private String nomEtablissement;
    private String nomDomaine;
    private String nomSecteur;
    private String nomVille;
    private BigDecimal tarifConsultation;
    private Boolean actif;
}
