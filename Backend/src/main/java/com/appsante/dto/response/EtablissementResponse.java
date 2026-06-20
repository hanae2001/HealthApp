package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EtablissementResponse {
    private Integer idEtablissement;
    private String nomEtablissement;
    private String adresse;
    private String telephone;
    private String nomSecteur;
    private String nomVille;
}
