package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PublicMedecinResponse {
    private Integer idMedecin;
    private String nom;
    private String prenom;
    private String nomDomaine;
    private Integer experienceAns;
    private BigDecimal noteMoyenne;
    private Integer nbAvis;
    private BigDecimal tarifConsultation;
}
