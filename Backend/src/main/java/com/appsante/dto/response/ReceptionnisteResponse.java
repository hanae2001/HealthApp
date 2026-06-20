package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceptionnisteResponse {
    private Integer idReceptionniste;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String nomEtablissement;
    private String nomSecteur;
    private String nomVille;
}
