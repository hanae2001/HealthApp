package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class SoinResponse {
    private Integer idSoin;
    private String nomMedecin;
    private String prenomMedecin;
    private String typeSoin;
    private String dateSoin;
    private String cout;
    private String statut;
}
